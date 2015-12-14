package insert;

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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import util.Dates;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Insercion extends Task {

    Fichero file;
    List<Ins> multas;
    List<Doc> documentos;

    public Insercion() {
        multas = new ArrayList();
        documentos = new ArrayList();
    }

    @Override
    protected Void call() {
        updateTitle("Loading Files");
        file = new Fichero();

        load();
        insert();

//        for (int i = 0; i < 10000; i++) {
//                updateProgress(i, 10000);
//                updateMessage("Iteracion "+i);
//        }
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
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    private void insert() {
        try {
            insertDocumentos();
        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
        insertMultas();
    }

    private void insertDocumentos() throws SQLException, FileNotFoundException {
        updateTitle("Loading Documentos");
        Doc aux;
        File fl = new File("temp.pdf");

        Sql bd = new Sql(con);
        String sql = "INSERT INTO historico.documento (id,codigo,data) VALUES (?, ?, ?)";
        PreparedStatement stmt = bd.con.prepareStatement(sql);
        bd.con.setAutoCommit(false);

        for (int i = 0; i < documentos.size(); i++) {
            updateProgress(i, documentos.size());
            updateMessage("Cargando documento " + i + " de " + documentos.size());
            aux = documentos.get(i);
            Varios.descargaArchivo(aux.getLink(), fl);

            stmt.setString(1, aux.getId());
            stmt.setString(2, aux.getCodigo());
            FileInputStream fis = new FileInputStream(fl);
            stmt.setBinaryStream(3, fis, (int) fl.length());
            stmt.execute();
        }
        
        bd.con.commit();

    }

    private void insertMultas() {
        updateTitle("Loading Multas");
    }

    private void load() {
        updateTitle("Parsing .ins Files");
        loadIns();
        updateTitle("Parsing .BB1 Files");
//        loadBB1();
    }

    private void loadBB1() {
        System.out.println("Procesando " + file.getBB1().size() + " archivos .ins ");
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
        System.out.println("Procesando " + file.getIns().size() + " archivos .ins ");
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
}
