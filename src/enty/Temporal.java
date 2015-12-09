package enty;

import java.util.Date;

/**
 *
 * @author Agarimo
 */
public class Temporal {

    private int id;
    private String codigoSancion;
    private Date fechaPublicacion;
    private String organismo;
    private String boe;
    private String fase;
    private String tipoJuridico;
    private int plazo;
    private String expediente;
    private Date fechaMulta;
    private String articulo;
    private String cif;
    private String nombre;
    private String poblacion;
    private String matricula;
    private String euros;
    private String puntos;
    private String linea;
    private String link;

    public Temporal() {

    }

    public Temporal(String codigoSancion, Date fechaPublicacion, String organismo, String boe, String fase, String tipoJuridico, int plazo, String expediente, Date fechaMulta, String articulo, String cif, String nombre, String poblacion, String matricula, String euros, String puntos, String linea, String link) {
        this.codigoSancion = codigoSancion;
        this.fechaPublicacion = fechaPublicacion;
        this.organismo = organismo;
        this.boe = boe;
        this.fase = fase;
        this.tipoJuridico = tipoJuridico;
        this.plazo = plazo;
        this.expediente = expediente;
        this.fechaMulta = fechaMulta;
        this.articulo = articulo;
        this.cif = cif;
        this.nombre = nombre;
        this.poblacion = poblacion;
        this.matricula = matricula;
        this.euros = euros;
        this.puntos = puntos;
        this.linea = linea;
        this.link = link;
    }

    public Temporal(int id, String codigoSancion, Date fechaPublicacion, String organismo, String boe, String fase, String tipoJuridico, int plazo, String expediente, Date fechaMulta, String articulo, String cif, String nombre, String poblacion, String matricula, String euros, String puntos, String linea, String link) {
        this.id = id;
        this.codigoSancion = codigoSancion;
        this.fechaPublicacion = fechaPublicacion;
        this.organismo = organismo;
        this.boe = boe;
        this.fase = fase;
        this.tipoJuridico = tipoJuridico;
        this.plazo = plazo;
        this.expediente = expediente;
        this.fechaMulta = fechaMulta;
        this.articulo = articulo;
        this.cif = cif;
        this.nombre = nombre;
        this.poblacion = poblacion;
        this.matricula = matricula;
        this.euros = euros;
        this.puntos = puntos;
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

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getBoe() {
        return boe;
    }

    public void setBoe(String boe) {
        this.boe = boe;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getTipoJuridico() {
        return tipoJuridico;
    }

    public void setTipoJuridico(String tipoJuridico) {
        this.tipoJuridico = tipoJuridico;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEuros() {
        return euros;
    }

    public void setEuros(String euros) {
        this.euros = euros;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
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
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.linea != null ? this.linea.hashCode() : 0);
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
        final Temporal other = (Temporal) obj;
        if ((this.linea == null) ? (other.linea != null) : !this.linea.equals(other.linea)) {
            return false;
        }
        return true;
    }

    public String SQLCrear() {

        if (fechaMulta != null) {
            String query = "INSERT into historico.temp_historico (codigoSancion,fecha_publicacion,organismo,boe,fase,tipojuridico,plazo,expediente,fecha_multa,articulo,cif,nombre,poblacion,matricula,euros,puntos,linea,link) values("
                    + util.Varios.entrecomillar(this.codigoSancion) + ","
                    + util.Varios.entrecomillar(util.Dates.imprimeFecha(this.fechaPublicacion)) + ","
                    + util.Varios.entrecomillar(this.organismo) + ","
                    + util.Varios.entrecomillar(this.boe) + ","
                    + util.Varios.entrecomillar(this.fase) + ","
                    + util.Varios.entrecomillar(this.tipoJuridico) + ","
                    + this.plazo + ","
                    + util.Varios.entrecomillar(this.expediente) + ","
                    + util.Varios.entrecomillar(util.Dates.imprimeFecha(this.fechaMulta)) + ","
                    + util.Varios.entrecomillar(this.articulo) + ","
                    + util.Varios.entrecomillar(this.cif) + ","
                    + util.Varios.entrecomillar(this.nombre) + ","
                    + util.Varios.entrecomillar(this.poblacion) + ","
                    + util.Varios.entrecomillar(this.matricula) + ","
                    + util.Varios.entrecomillar(this.euros) + ","
                    + util.Varios.entrecomillar(this.puntos) + ","
                    + util.Varios.entrecomillar(this.linea) + ","
                    + util.Varios.entrecomillar(this.link)
                    + ");";
            return query;
        } else {
            String query = "INSERT into historico.temp_historico (codigoSancion,fecha_publicacion,organismo,boe,fase,tipojuridico,plazo,expediente,articulo,cif,nombre,poblacion,matricula,euros,puntos,linea,link) values("
                    + util.Varios.entrecomillar(this.codigoSancion) + ","
                    + util.Varios.entrecomillar(util.Dates.imprimeFecha(this.fechaPublicacion)) + ","
                    + util.Varios.entrecomillar(this.organismo) + ","
                    + util.Varios.entrecomillar(this.boe) + ","
                    + util.Varios.entrecomillar(this.fase) + ","
                    + util.Varios.entrecomillar(this.tipoJuridico) + ","
                    + this.plazo + ","
                    + util.Varios.entrecomillar(this.expediente) + ","
                    + util.Varios.entrecomillar(this.articulo) + ","
                    + util.Varios.entrecomillar(this.cif) + ","
                    + util.Varios.entrecomillar(this.nombre) + ","
                    + util.Varios.entrecomillar(this.poblacion) + ","
                    + util.Varios.entrecomillar(this.matricula) + ","
                    + util.Varios.entrecomillar(this.euros) + ","
                    + util.Varios.entrecomillar(this.puntos) + ","
                    + util.Varios.entrecomillar(this.linea) + ","
                    + util.Varios.entrecomillar(this.link)
                    + ");";
            return query;
        }
    }
}
