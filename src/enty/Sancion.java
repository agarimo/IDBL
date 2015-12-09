package enty;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Agárimo
 */
public final class Sancion {

    int id;
    String codigoSancion;
    String expediente;
    Date fechaMulta;
    String articulo;
    String cuantia;
    String puntos;
    String nombre;
    String localidad;
    String linea;
    String link;

    public Sancion() {

    }

    public Sancion(int id) {
        this.id = id;
    }

    public Sancion(int idSancion, String codigoSancion, String expediente, Date fechaMulta, String articulo, String cuantia, String puntos, String nombre, String localidad, String linea, String link) {
        this.id = idSancion;
        this.codigoSancion = codigoSancion;
        this.expediente = expediente;
        this.fechaMulta = fechaMulta;
        this.articulo = articulo;
        this.cuantia = cuantia;
        this.puntos = puntos;
        this.nombre = nombre;
        this.localidad = localidad;
        this.linea = linea;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoSancion() {
        return codigoSancion;
    }

    public void setCodigoSancion(String codigoSancion) {
        this.codigoSancion = codigoSancion;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getFechaMulta() {
        return fechaMulta;
    }

    public void setFechaMulta(Date fechaMulta) {
        this.fechaMulta = fechaMulta;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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

    @Override
    public String toString() {
        return "Sancion{" + "expediente=" + expediente + ", fechaMulta=" + fechaMulta + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.codigoSancion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sancion other = (Sancion) obj;
        if (!Objects.equals(this.codigoSancion, other.codigoSancion)) {
            return false;
        }
        return true;
    }

    public String crearSancion() {
        String query;
        if (fechaMulta != null) {
            query = "INSERT into historico.sancion (codigoSancion,expediente,fechaMulta,articulo,cuantia,puntos,nombre,localidad,linea,link) values("
                    + util.Varios.entrecomillar(codigoSancion) + ","
                    + util.Varios.entrecomillar(expediente) + ","
                    + util.Varios.entrecomillar(util.Dates.imprimeFecha(fechaMulta)) + ","
                    + util.Varios.entrecomillar(articulo) + ","
                    + util.Varios.entrecomillar(cuantia) + ","
                    + util.Varios.entrecomillar(puntos) + ","
                    + util.Varios.entrecomillar(nombre) + ","
                    + util.Varios.entrecomillar(localidad) + ","
                    + util.Varios.entrecomillar(linea) + ","
                    + util.Varios.entrecomillar(link)
                    + ");";
        } else {
            query = "INSERT into historico.sancion (codigoSancion,expediente,articulo,cuantia,puntos,nombre,localidad,linea,link) values("
                    + util.Varios.entrecomillar(codigoSancion) + ","
                    + util.Varios.entrecomillar(expediente) + ","
                    + util.Varios.entrecomillar(articulo) + ","
                    + util.Varios.entrecomillar(cuantia) + ","
                    + util.Varios.entrecomillar(puntos) + ","
                    + util.Varios.entrecomillar(nombre) + ","
                    + util.Varios.entrecomillar(localidad) + ","
                    + util.Varios.entrecomillar(linea) + ","
                    + util.Varios.entrecomillar(link)
                    + ");";
        }
        return query;
    }

    public String buscarSancion() {
        String query = "SELECT idSancion FROM historico.sancion WHERE expediente=" + util.Varios.entrecomillar(this.expediente) + ";";
        return query;
    }
}
