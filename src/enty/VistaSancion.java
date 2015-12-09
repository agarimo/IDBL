package enty;

import java.util.Date;

/**
 *
 * @author Agarimo
 */
public class VistaSancion {
    String boe;
    String origen;
    Date publicacion;
    String nif;
    String nombre;
    String matricula;
    String expediente;
    String articulo;
    String puntos;
    String cuantia;
    String linea;
    String link;
    
    public VistaSancion(){
        
    }

    public VistaSancion(String boe, String origen, Date publicacion, String nif, String nombre, String matricula, String expediente, String articulo, String puntos, String cuantia, String linea, String link) {
        this.boe = boe;
        this.origen = origen;
        this.publicacion = publicacion;
        this.nif = nif;
        this.nombre = nombre;
        this.matricula = matricula;
        this.expediente = expediente;
        this.articulo = articulo;
        this.puntos = puntos;
        this.cuantia = cuantia;
        this.linea = linea;
        this.link = link;
    }

    public String getBoe() {
        return boe;
    }

    public void setBoe(String boe) {
        this.boe = boe;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Date getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Date publicacion) {
        this.publicacion = publicacion;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
