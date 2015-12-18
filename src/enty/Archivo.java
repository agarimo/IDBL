package enty;

import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Archivo {
    String nombre;
    long size;
    int lineas;
    
    public Archivo(){
        
    }

    public Archivo(String nombre, int size, int lineas) {
        this.nombre = nombre;
        this.size = size;
        this.lineas = lineas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getLineas() {
        return lineas;
    }

    public void setLineas(int lineas) {
        this.lineas = lineas;
    }
    
    public String SQLCrear(){
        return "INSERT INTO idbl_stats.archivos (fecha,nombre,size,lineas) values("
                + "CURDATE(),"
                + Varios.entrecomillar(this.nombre) + ","
                + this.size + ","
                + this.lineas
                + ");";
    }
}
