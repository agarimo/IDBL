package val;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class Validador {

    String nombre;
    String path;
    File destino;

    public Validador(File archivo) throws SQLException, IOException {
        this.nombre = archivo.getName();
        this.path = archivo.getParent();
        preparaDestino();
        procesaArchivo(archivo, this.destino);
    }

    private void preparaDestino() throws IOException {
        this.destino = new File(path, this.nombre.replace(".bb1", ".bb2"));

        if (!this.destino.exists()) {
            this.destino.createNewFile();
        } else {
            this.destino.delete();
            preparaDestino();
        }
    }

    private void procesaArchivo(File archivo, File destino) {
        FileReader fr = null;
        FileWriter fichero = null;

        try {
            preparaDestino();
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            fichero = new FileWriter(destino, true);
            PrintWriter pw = new PrintWriter(fichero);
            String linea;
            while ((linea = br.readLine()) != null) {
                Linea aux = new Linea(linea);
                pw.println(aux.toString());
            }
        } catch (IOException | SQLException e) {
//            Error.ioe(null, e.getMessage());
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
//                Error.ioe(null, e2.getMessage());
            }
        }
    }
}
