package enty;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Agarimo
 */
public class Stats {
    private Carga carga;
    private List<Archivo> archivos;
    
    
    public Stats(){
        carga= new Carga();
        archivos= new ArrayList();
    }

    public Carga getCarga() {
        return carga;
    }

    public void setCarga(Carga carga) {
        this.carga = carga;
    }

    public List<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<Archivo> archivos) {
        this.archivos = archivos;
    }
    
    public void addArchivo(Archivo aux){
        archivos.add(aux);
    }
}
