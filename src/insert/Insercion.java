package insert;

import enty.Ins;
import enty.Doc;
import idbl.Var;
import static idbl.Var.con;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Dates;
import util.Sql;
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

    public Insercion() {
        multas = new ArrayList();
        documentos = new ArrayList();
    }

    @Override
    protected Void call() {
        file = new Fichero();
        load();
        insert();
        sqlTask();

        return null;
    }

    private List<String[]> cargaLineas(File archivo) {
        List<String[]> aux = new ArrayList<>();
        try (FileReader fr = new FileReader(archivo); BufferedReader br = new BufferedReader(fr)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                aux.add(linea.split("\\|"));
            }
        } catch (FileNotFoundException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    private void insert() {
        try {
            insertDocumentos();
        } catch (SQLException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            insertMultas();
        } catch (SQLException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertDocumentos() throws SQLException, FileNotFoundException {
        updateTitle("Loading Documentos");
        Doc aux;
        File fl = new File("temp.pdf");

        bd = new Sql(con);
        String sql = "INSERT INTO historico.documento (id,codigo,data) VALUES (?, ?, ?)";
        PreparedStatement st = bd.con.prepareStatement(sql);
        bd.con.setAutoCommit(false);

        for (int i = 0; i < documentos.size(); i++) {
            updateProgress((i + 1), documentos.size());
            updateMessage("Cargando documento " + (i + 1) + " de " + documentos.size());
            aux = documentos.get(i);
            Varios.descargaArchivo(aux.getLink(), fl);

            st.setString(1, aux.getId());
            st.setString(2, aux.getCodigo());
            FileInputStream fis = new FileInputStream(fl);
            st.setBinaryStream(3, fis, (int) fl.length());
            st.execute();
        }

        bd.con.commit();
        fl.delete();
        bd.close();
        updateProgress(1, -1);
        updateMessage("");
    }

    private void insertMultas() throws SQLException {
        updateTitle("Loading Multas");
        Ins aux;

        bd = new Sql(con);
        String sql = "INSERT INTO historico.temp_historico (codigoSancion,fecha_publicacion,organismo,boe,fase,tipojuridico,plazo,expediente,"
                + "fecha_multa,articulo,cif,nombre,poblacion,matricula,euros,puntos,linea) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = bd.con.prepareStatement(sql);
        bd.con.setAutoCommit(false);

        for (int i = 0; i < multas.size(); i++) {
            aux = multas.get(i);
            updateProgress((i + 1), multas.size());
            updateMessage("Cargando multa " + (i + 1) + " de " + multas.size());

            st.setString(1, aux.getCodigoSancion());
            st.setString(2, Dates.imprimeFecha(aux.getFechaPublicacion()));
            st.setString(3, aux.getOrganismo());
            st.setString(4, aux.getnBoe());
            st.setString(5, aux.getFase());
            st.setString(6, aux.getTipoJuridico());
            st.setInt(7, Integer.parseInt(aux.getPlazo()));
            st.setString(8, aux.getExpediente());
            st.setString(9, Dates.imprimeFecha(aux.getFechaMulta()));
            st.setString(10, aux.getArticulo());
            st.setString(11, aux.getNif());
            st.setString(12, aux.getSancionado());
            st.setString(13, aux.getLocalidad());
            st.setString(14, aux.getMatricula());
            st.setString(15, aux.getCuantia());
            st.setString(16, aux.getPuntos());
            st.setString(17, aux.getLinea());

            st.execute();
        }

        updateProgress(1, -1);
        updateMessage("Commiteando en BBDD");
        bd.con.commit();
        updateMessage("");
    }

    private void load() {
        updateTitle("Parsing .ins Files");
        loadIns();
        updateTitle("Parsing .BB1 Files");
        loadBB1();
    }

    private void loadBB1() {
        String[] split;
        List<String[]> list = new ArrayList();
        File aux;
        Iterator<File> it = file.getBB1().iterator();

        while (it.hasNext()) {
            aux = it.next();
            list.addAll(cargaLineas(aux));
        }

        Iterator<String[]> itt = list.iterator();

        while (itt.hasNext()) {
            split = itt.next();
            splitBB1(split);
        }
    }

    private void loadIns() {
        String[] split;
        List<String[]> list = new ArrayList();
        File aux;
        Iterator<File> it = file.getIns().iterator();

        while (it.hasNext()) {
            aux = it.next();
            list.addAll(cargaLineas(aux));
        }

        Iterator<String[]> itt = list.iterator();

        while (itt.hasNext()) {
            split = itt.next();

            if (split.length == 3) {
                splitDoc(split);
            } else if (split.length == 17) {
                splitIns(split);
            }
        }
    }

    private void splitBB1(String[] split) {
        Ins aux = new Ins();

        aux.setCodigoSancion(split[2].trim() + split[8].trim());
        aux.setFechaPublicacion(util.Dates.formatFecha(split[1].trim(), "dd/MM/yyyy"));
        aux.setOrganismo(split[16].trim());
        aux.setnBoe(split[2].trim() + split[22].trim());
        aux.setFase(split[3]);
        aux.setTipoJuridico(split[4].trim());
        aux.setPlazo(split[5]);
        aux.setExpediente(split[10].trim());
        aux.setFechaMulta(splitBB1_ParseFecha(split[11].trim()));
        aux.setArticulo(split[12].trim());
        aux.setNif(split[15].trim());
        aux.setSancionado(split[13].trim());
        aux.setLocalidad(split[25]);
        aux.setMatricula(split[14].trim());
        aux.setCuantia(split[17].trim());
        aux.setPuntos(split[18].trim());
        aux.setLinea(split[23].trim());
//        aux.setLink(split[24].trim());

    }

    private Date splitBB1_ParseFecha(String fecha) {
        Date aux;
        if (fecha.equals("")) {
            return null;
        } else {
            aux = util.Dates.formatFecha(fecha, "ddMMyy");
            if (aux == null) {
                aux = util.Dates.formatFecha(fecha, "MMddyy");
            }
        }
        return aux;
    }

    private void splitDoc(String[] split) {
        Doc aux = new Doc();

        aux.setId(split[0]);
        aux.setCodigo(split[1]);
        aux.setLink(split[2]);

        documentos.add(aux);
    }

    private void splitIns(String[] split) {
        Ins aux = new Ins();

        aux.setFechaPublicacion(Dates.formatFecha(split[0], "yyyy-MM-dd"));
        aux.setnBoe(split[1]);
        aux.setOrganismo(split[2]);
        aux.setFase(split[3]);
        aux.setPlazo(split[4]);
        aux.setCodigoSancion(split[5]);
        aux.setExpediente(split[6]);
        aux.setFechaMulta(Dates.formatFecha(split[7], "yyyy-MM-dd"));
        aux.setArticulo(split[8]);
        aux.setNif(split[9]);
        aux.setTipoJuridico(split[10]);
        aux.setSancionado(split[11]);
        aux.setMatricula(split[12]);
        aux.setCuantia(split[13]);
        aux.setPuntos(split[14]);
        aux.setLocalidad(split[15]);
        aux.setLinea(split[16]);

        multas.add(aux);
    }

    private void sqlTask() {
        updateTitle("Ejecutando sqlTask");
        
        String sqlTask;
        String query;
        for (int i = 0; i < Var.sqlTask.length; i++) {
            sqlTask = Var.sqlTask[i][0];
            query = Var.sqlTask[i][1];
            updateMessage("Ejecutando "+sqlTask);
            
            if(!sqlTask_ejecutar(query)){
                break;
            }
        }
    }

    private boolean sqlTask_ejecutar(String query) {
        try {
            bd = new Sql(con);
            bd.ejecutar(query);
            bd.close();
            return true;
        } catch (SQLException ex) {
            log.error(ex);
//            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
