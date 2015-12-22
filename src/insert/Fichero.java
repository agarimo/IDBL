package insert;

import idbl.Var;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import util.Files;

/**
 *
 * @author Agarimo
 */
public class Fichero {

    private final List<File> ins;

    public Fichero() {
        ins = new ArrayList();
        loadFiles(Var.fileData);
    }

    private void loadFiles(File file) {
        File[] ficheros = file.listFiles();

        for (File fichero : ficheros) {
            if (fichero.isDirectory()) {
                loadFiles(fichero);
            } else {
                if (fichero.getName().contains(".ins") || fichero.getName().contains(".INS")) {
                    ins.add(fichero);
                } else {
                    Files.moverArchivo(fichero, new File("dsc", fichero.getName()));
                }
            }
        }
    }

    public List<File> getIns() {
        return ins;
    }
}
