package insrt;

import enty.Timming;
import idbl.Var;
import java.io.File;
import util.*;

/**
 *
 * @author Agarimo
 */
public class InsercionC extends Thread {

    Sql bd;
    public static Insercion win;
    private final Fichero fichero;
    private SqlTask sql;

    public InsercionC() {
//        win = new Insercion();
//        win.setLocationRelativeTo(null);
//        win.setVisible(true);
        Var.tm = new Timming(Dates.curdate());
        fichero = new Fichero(new File("data"));
    }

    @Override
    public void run() {
        fichero.start();
        fichero.procesaFichero();
        sql = new SqlTask();
        limpiar();
//        win.dispose();
    }

    private void limpiar() {
        File block = new File("block.blk");
        File aux = new File("data");
        File[] archivos = aux.listFiles();

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                Files.borraDirectorio(archivo);
            } else {
                archivo.delete();
            }
        }
        block.delete();
    }
}
