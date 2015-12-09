package enty;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


/**
 *
 * @author Agarimo
 */
public class Multa {

    private int idMulta;
    private int idBoletin;
    private int idMatricula;
    private int idSancionado;
    private String idSancion;
    private String fase;
    private int plazo;
    private Date fechaEntrada;
    private Date fechaVencimiento;

    public Multa() {
        
    }

    public Multa(int idMulta, int idBoletin, int idMatricula, int idSancionado, String idSancion,
            String fase, int plazo,Date fechaEntrada, Date fechaVencimiento) {
        this.idMulta = idMulta;
        this.idBoletin = idBoletin;
        this.idMatricula = idMatricula;
        this.idSancionado = idSancionado;
        this.idSancion = idSancion;
        this.fase = fase;
        this.plazo = plazo;
        this.fechaEntrada = fechaEntrada;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(int idMulta) {
        this.idMulta = idMulta;
    }

    public int getIdBoletin() {
        return idBoletin;
    }

    public void setIdBoletin(int idBoletin) {
        this.idBoletin = idBoletin;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public int getIdSancionado() {
        return idSancionado;
    }

    public void setIdSancionado(int idSancionado) {
        this.idSancionado = idSancionado;
    }

    public String getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(String idSancion) {
        this.idSancion = idSancion;
    }

    public Date calculaFecha(int dias, Date fecha) {
        Calendar aux = Calendar.getInstance();
        aux.setTime(fecha);
        aux.add(Calendar.DATE, dias);

        return aux.getTime();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.idSancion);
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
        final Multa other = (Multa) obj;
        return Objects.equals(this.idSancion, other.idSancion);
    }

    public String crearMulta() {
        String query = "INSERT into historico.multa (idBoletin,idMatricula,idSancionado,idSancion,fase,plazo,fechaEntrada,fechaVencimiento) values("
                + idBoletin + ","
                + idMatricula + ","
                + idSancionado + ","
                + util.Varios.entrecomillar(idSancion) + ","
                + util.Varios.entrecomillar(fase) + ","
                + plazo + ","
                + "curdate(),"
                + util.Varios.entrecomillar(util.Dates.imprimeFecha(calculaFecha(plazo,fechaEntrada)))
                + ");";
        return query;
    }
}
