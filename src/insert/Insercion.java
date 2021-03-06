package insert;

import enty.Archivo;
import enty.Ins;
import enty.Doc;
import enty.Fase;
import enty.Stats;
import files.Util;
import idbl.Mail;
import idbl.Var;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.Sql;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Insercion extends Task {

    private static final Logger log = LogManager.getLogger(Insercion.class);
    Fichero file;
    List<Ins> multas;
    List<Doc> documentos;
    Sql bd;
    Stats stats;

    private boolean load;
    private boolean insertD;
    private boolean insert;
    private boolean errorInsert;
    private boolean validar;
    private boolean sqlTask;

    public Insercion() {
        load = false;
        insertD = false;
        insert = false;
        validar = false;
        sqlTask = false;
        multas = new ArrayList();
        documentos = new ArrayList();
        stats = new Stats();
    }

    @Override
    protected Void call() {
        log.info("Iniciando carga IDBL");
        load();
        if (load) {
            callDocuments();
            insert();
            if (insert) {
                validar();
                if (validar) {
                    sqlTask();
                    stats.getCarga().setFin();
                    if (sqlTask) {
                        stats.getCarga().setMultas(multas.size());
                        stats.getCarga().setDocumentos(documentos.size());
                        stats.getCarga().setStatus("OK");
                    } else {
                        callError();
                    }
                }
            } else if (errorInsert) {
                callError();
            } else {
                stats.getCarga().setFin();
                sqlTask = true;
            }
        }

        xit();
        return null;
    }

    private void callDocuments() {
        if (Var.insercionDoc) {
            insertD();

            if (!insertD) {
                callVolcadoDoc();
                if (Var.mailAviso) {
                    Mail mail = new Mail("DOCUMENT ERROR", "Se ha producido un error en la carga de Documentos.");
                    try {
                        mail.run();
                    } catch (Exception ex) {
                        log.warn("MAIL - " + ex);
                    }
                }
            }
        }
    }

    private void callError() {
        if (Var.mailAviso) {
            Mail mail = new Mail();
            try {
                mail.run();
            } catch (Exception ex) {
                log.warn("MAIL - " + ex);
            }

            try {
                bd = new Sql(Var.con);
                bd.ejecutar(Var.sqlTask[7][1]);
                bd.close();
            } catch (SQLException ex) {
                log.warn("CALL.ERROR - " + ex);
            }
        }
    }

    private void callVolcadoDoc() {
        Doc doc;
        File aux = new File(Var.dscData, "docPendiente.ins");

        if (!aux.exists()) {
            try {
                aux.createNewFile();
            } catch (IOException ex) {
                log.warn("VOLCADO DOC - " + ex);
            }
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Doc> it = documentos.iterator();

        while (it.hasNext()) {
            doc = it.next();
            sb.append(doc.toString());
            sb.append(System.lineSeparator());
        }

        Util.anexaArchivo(aux, sb.toString().trim());
    }

    private void insert() {
        if (multas.isEmpty()) {
            insert = false;
        } else {
            try {
                insertMultas();
                insert = true;
                errorInsert = false;
            } catch (SQLException ex) {
                errorInsert = true;
                log.error("INSERT MULTAS - " + ex);
            }
        }
    }

    private void insertD() {
        try {
            insertDClearDuplicates();
            insertDocumentos();
            insertD = true;
        } catch (SQLException | IOException ex) {
            log.error("INSERT DOC - " + ex);
        }
    }

    private void insertDClearDuplicates() throws SQLException {
        updateTitle("LIMPIANDO DOCUMENTOS");
        Doc documento;
        List<Doc> cleanList = new ArrayList();

        bd = new Sql(Var.con);

        for (int i = 0; i < documentos.size(); i++) {
            documento = documentos.get(i);
            updateProgress((i + 1), documentos.size());
            updateMessage("Comprobando documento " + (i + 1) + " de " + documentos.size());

            if (bd.buscar(documento.SQLBuscar()) == -1) {
                cleanList.add(documento);
            }
        }

        bd.close();
        documentos.clear();
        documentos.addAll(cleanList);

    }

    private void insertDocumentos() throws SQLException, FileNotFoundException, IOException {
        updateTitle("LOADING DOCUMENTOS");
        Doc aux;
        File dir;
        File fl;

        bd = new Sql(Var.con);
        String sql = "INSERT INTO idbl.documento (id,fecha,codigo) VALUES (?, ?, ?)";
        PreparedStatement st = bd.getCon().prepareStatement(sql);
        bd.getCon().setAutoCommit(true);

        for (int i = 0; i < documentos.size(); i++) {
            updateProgress((i + 1), documentos.size());

            updateMessage("Descargando documento " + (i + 1) + " de " + documentos.size());
            aux = documentos.get(i);
            dir = new File(Var.ftpFileSystem, aux.getFecha());
            dir.mkdirs();
            fl = new File(dir, aux.getCodigo() + ".pdf");
            Varios.descargaArchivo(aux.getLink(), fl);

            updateMessage("Subiendo documento " + (i + 1) + " de " + documentos.size());
            st.setString(1, aux.getId());
            st.setString(2, aux.getFecha());
            st.setString(3, aux.getCodigo());
            st.execute();
        }

        bd.close();
        updateProgress(1, -1);
        updateMessage("");
    }

    private void insertMultas() throws SQLException {
        updateTitle("LOADING MULTAS");
        Ins aux;

        bd = new Sql(Var.con);
        String sql = "INSERT INTO idbl.temp_idbl (codigo,fecha_publicacion,fecha_vencimiento,organismo,n_boe,fase,tipo_juridico,plazo,expediente,"
                + "fecha_multa,articulo,cif,nombre,localidad,matricula,cuantia,puntos,linea) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = bd.getCon().prepareStatement(sql);
        bd.getCon().setAutoCommit(false);

        for (int i = 0; i < multas.size(); i++) {
            aux = multas.get(i);
            updateProgress((i + 1), multas.size());
            updateMessage("Cargando multa " + (i + 1) + " de " + multas.size());

            st.setString(1, aux.getCodigoSancion());
            st.setString(2, Dates.imprimeFecha(aux.getFechaPublicacion()));
            st.setString(3, Dates.imprimeFecha(aux.getFechaVencimiento()));
            st.setString(4, aux.getOrganismo());
            st.setString(5, aux.getnBoe());
            st.setString(6, aux.getFase());
            st.setString(7, aux.getTipoJuridico());
            st.setString(8, aux.getPlazo());
            st.setString(9, aux.getExpediente());
            st.setString(10, Dates.imprimeFecha(aux.getFechaMulta()));
            st.setString(11, aux.getArticulo());
            st.setString(12, aux.getNif());
            st.setString(13, aux.getSancionado());
            st.setString(14, aux.getLocalidad());
            st.setString(15, aux.getMatricula());
            st.setString(16, aux.getCuantia());
            st.setString(17, aux.getPuntos());
            st.setString(18, aux.getLinea());

            st.execute();
        }

        updateProgress(1, -1);
        updateMessage("Commiteando en BBDD");
        bd.getCon().commit();
        bd.close();
        updateMessage("");
    }

    private void load() {
        try {
            updateTitle("LOADING FILES");
            file = new Fichero();
            updateTitle("PARSING FILES");
            loadIns();
            load = true;
        } catch (Exception ex) {
            log.error("LOAD LINES - " + ex);
        }
    }

    private void loadIns() throws IOException {
        String[] split;
        List<String[]> list = new ArrayList();
        File aux;
        Iterator<File> it = file.getIns().iterator();

        while (it.hasNext()) {
            aux = it.next();
            list.addAll(loadInsFile(aux));
        }

        Iterator<String[]> itt = list.iterator();

        while (itt.hasNext()) {
            split = itt.next();

            if (split.length == 4) {
                splitDoc(split);
            } else if (split.length == 17) {
                splitIns(split);
            }
        }

        if (file.isDsc()) {
            Mail mail = new Mail("FICHEROS DESCARTADOS", "Se han descartado ficheros durante la carga\n"
                    + "Compruebe el directorio /Dsc.");
            try {
                mail.run();
            } catch (Exception ex) {
                log.warn("MAIL - " + ex);
            }
        }
    }

    private List<String[]> loadInsFile(File archivo) throws FileNotFoundException, IOException {
        List<String[]> aux = new ArrayList<>();
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea;

        while ((linea = br.readLine()) != null) {
            aux.add(linea.split("\\|"));
        }
        loadInsStats(archivo, aux.size());

        br.close();
        fr.close();

        return aux;
    }

    private void loadInsStats(File file, int total) {
        Archivo aux = new Archivo();
        aux.setNombre(file.getName());
        aux.setSize(file.length());
        aux.setLineas(total);

        stats.addArchivo(aux);
    }

    private void splitDoc(String[] split) {
        Doc aux = new Doc();

        aux.setId(split[0]);
        aux.setFecha(split[1]);
        aux.setCodigo(split[2]);
        aux.setLink(split[3]);

        documentos.add(aux);
    }

    private void splitIns(String[] split) {
        Ins aux = new Ins();

        aux.setFechaPublicacion(Dates.formatFecha(split[0].trim(), "yyyy-MM-dd"));
        aux.setnBoe(split[1].trim());
        aux.setOrganismo(split[2].trim());
        aux.setFase(split[3].trim());
        aux.setPlazo(split[4].trim());
        aux.setCodigoSancion(split[5].trim());
        aux.setExpediente(split[6].trim());
        aux.setFechaMulta(Dates.formatFecha(split[7].trim(), "yyyy-MM-dd"));
        aux.setArticulo(split[8].trim());
        aux.setNif(split[9].trim());
        aux.setTipoJuridico(split[10].trim());
        aux.setSancionado(split[11].trim());
        aux.setMatricula(split[12].trim());
        aux.setCuantia(split[13].trim());
        aux.setPuntos(splitIns_valPuntos(split[14].trim()));
        aux.setLocalidad(split[15].trim());
        aux.setLinea(split[16].trim());
        aux.setFechaVencimiento();

        multas.add(aux);
    }

    private String splitIns_valPuntos(String puntos) {
        String aux = "";

        try {
            int a = Integer.parseInt(puntos);

            if (a == 0 || a == 2 || a == 3 || a == 4 || a == 6) {
                aux = Integer.toString(a);
            }
        } catch (Exception ex) {
            aux = "";
        }

        return aux;
    }

    private void sqlTask() {
        updateTitle("RUNNING SQLTASK");

        if (sqlTaskClean()) {
            String task;
            String query;

            for (int i = 0; i < Var.sqlTask.length; i++) {
                task = Var.sqlTask[i][0];
                query = Var.sqlTask[i][1];
                updateMessage("Ejecutando " + task);

                if (!sqlTask_ejecutar(task, query)) {
                    updateTitle("ERROR en " + task);
                    updateMessage("Consulte el log para mas información");
                    updateProgress(0, 0);
                    sqlTask = false;
                    break;
                } else {
                    sqlTask = true;
                }
            }
        } else {
            sqlTask = true;
        }
    }

    private boolean sqlTaskClean() {
        String status = "CLEAN DUPLICATES";
        String query = "DELETE FROM idbl.temp_idbl WHERE codigo "
                + "IN "
                + "(SELECT codigo FROM idbl.sancion)";

        if (sqlTask_ejecutar(status, query)) {
            return sqlTaskCleanCount();
        } else {
            return false;
        }

    }

    private boolean sqlTaskCleanCount() {
        int count;
        try {
            bd = new Sql(Var.con);
            count = bd.getInt("SELECT count(*) FROM idbl.temp_idbl;");
            bd.close();

            return count != 0;
        } catch (SQLException ex) {
            log.error(sqlTask + " - " + ex);
            return false;
        }
    }

    private boolean sqlTask_ejecutar(String sqlTask, String query) {
        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
            return true;
        } catch (SQLException ex) {
            log.error(sqlTask + " - " + ex);
            return false;
        }
    }

    private void validar() {
        updateTitle("RUNNING VALIDAR");
        Fase fase;
        Iterator<Fase> it = Var.listFases.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                fase = it.next();
                updateMessage("Ejecutando FASE " + fase.getId() + " ECON");
                bd.ejecutar(fase.SQLECon());
                updateMessage("Ejecutando FASE " + fase.getId() + " ESIN");
                bd.ejecutar(fase.SQLESin());
                updateMessage("Ejecutando FASE " + fase.getId() + " PCON");
                bd.ejecutar(fase.SQLPCon());
                updateMessage("Ejecutando FASE " + fase.getId() + " PSIN");
                bd.ejecutar(fase.SQLPSin());
            }

            bd.close();
            validar = true;
        } catch (SQLException ex) {
            log.error("VALIDADOR - " + ex);
        }
    }

    private void xit() {
        if (sqlTask) {
            xitStats();
            file.cleanFiles();
        }

        log.info("Finalizado carga IDBL");

        Platform.runLater(() -> {
            System.exit(0);
        });
    }

    private void xitStats() {
        updateTitle("RUNNING STATS");
        updateMessage("");

        Archivo archivo;
        Iterator<Archivo> it = stats.getArchivos().iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                archivo = it.next();
                bd.ejecutar(archivo.SQLCrear());
            }

            bd.ejecutar(stats.getCarga().SQLCrear());
            bd.close();

        } catch (Exception ex) {
            log.error("STATS - " + ex);
        }
    }
}
