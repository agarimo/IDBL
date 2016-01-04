package enty;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.CalculaNif;
import util.Regex;

/**
 *
 * @author Agarimo
 */
public class Ins {

    private Date fechaPublicacion;
    private String nBoe;
    private String organismo;
    private String fase;
    private String plazo;
    private String codigoSancion;
    private String expediente;
    private Date fechaMulta;
    private String articulo;
    private String nif;
    private String tipoJuridico;
    private String sancionado;
    private String matricula;
    private String cuantia;
    private String puntos;
    private String localidad;
    private String linea;

    public Ins() {
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getnBoe() {
        return nBoe;
    }

    public void setnBoe(String nBoe) {
        this.nBoe = nBoe;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = checkNifPattern(checkNif(limpia(nif)));
    }

    public String getTipoJuridico() {
        return tipoJuridico;
    }

    public void setTipoJuridico(String tipoJuridico) {
        this.tipoJuridico = tipoJuridico;
    }

    public String getSancionado() {
        return sancionado;
    }

    public void setSancionado(String sancionado) {
        this.sancionado = sancionado;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    private String limpia(String str) {
        Pattern p = Pattern.compile("[^0-9A-Z]");
        Matcher m = p.matcher(str);

        if (m.find()) {
            str = m.replaceAll("");
        }
        return str.trim();
    }

    private String checkNif(String nif) {
        CalculaNif cn = new CalculaNif();
        String aux;

        try {
            if (nif.length() < 9) {
                aux = cn.calcular(nif);
            } else {
                if (cn.isvalido(nif)) {
                    aux = nif;
                } else {
                    aux = cn.calcular(nif);
                }
            }

            return aux;
        } catch (Exception ex) {
            return nif;
        }
    }

    private String checkNifPattern(String nif) {
        Regex rx = new Regex();
        String str;

        if (rx.isBuscar("[0-9]{4,7}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = add0(nif, 9);
        } else if (rx.isBuscar("[0]{1}[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(1, nif.length());
        } else if (rx.isBuscar("[XYZ]{1}[0-9]{4,6}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(0, 1);
            str = str + add0(nif.substring(1, nif.length()), 8);
        } else if (rx.isBuscar("[XYZ]{1}[0]{1}[0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(0, 1);
            str = str + nif.substring(2, nif.length());
        } else if (rx.isBuscar("[ABCDEFGHJKLMNPQRSUVW]{1}[0]{1}[0-9]{8}", nif)) {
            str = nif.substring(0, 1);
            str = str + nif.substring(2, nif.length());
        } else {
            str = nif;
        }

        return str;
    }

    private String add0(String aux, int size) {

        while (aux.length() < size) {
            aux = "0" + aux;
        }

        return aux;
    }
}
