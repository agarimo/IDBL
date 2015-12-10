package insert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import util.Files;

/**
 *
 * @author Agarimo
 */
public class Fichero {

    private final List<File> bb1;
    private final List<File> ins;
    
    public Fichero(File fichero){
        bb1 = new ArrayList();
        ins = new ArrayList();
        loadFiles(fichero);
    }

    private void loadFiles(File fichero) {
        File[] ficheros = fichero.listFiles();

        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                loadFiles(fichero1);
            } else {
                if (fichero1.getName().contains(".bb1")) {
                    bb1.add(fichero1);
                }else if(fichero1.getName().contains(".ins")){
                    ins.add(fichero1);
                } else {
                    Files.moverArchivo(fichero1, new File("dsc", fichero1.getName()));
                }
            }
        }
    }
    
    public void run(){
        
    }
    
    private void runBB1(){
        
    }
    
    private void runINS(){
        
    }
    
}
