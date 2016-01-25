package insert;

import idbl.Var;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.Files;

/**
 *
 * @author Agarimo
 */
public class Fichero {

    private final File block;
    private final List<File> ins;
    private boolean dsc;

    public Fichero() {
        block = new File("block.blk");

        try {
            block.createNewFile();
        } catch (IOException ex) {
            //
        }
        ins = new ArrayList();
        dsc = false;
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
                    Files.moverArchivo(fichero, new File(Var.dscData, fichero.getName()));
                    dsc = true;
                }
            }
        }
    }

    public void cleanFiles() {
        File pdf = new File("temp.pdf");
        File[] archivos = Var.fileData.listFiles();

        try {
            for (File archivo : archivos) {
                Files.borraDirectorio(archivo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (block.exists()) {
            block.delete();
        }

        if (pdf.exists()) {
            pdf.delete();
        }
    }

    public List<File> getIns() {
        return ins;
    }

    public boolean isDsc() {
        return dsc;
    }
}
