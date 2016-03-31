package enty;

import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.CalculaNif;
import util.Dates;
import util.Regex;

/**
 *
 * @author Agarimo
 */
public class Ins {

    private Date fechaPublicacion;
    private Date fechaVencimiento;
    private String nBoe;
    private String organismo;
    private String fase;
    private Plazo plazo;
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
        this.nBoe = resize(nBoe, 20);
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = resize(organismo, 100);
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = resize(fase, 3);
    }

    public String getPlazo() {
        return plazo.toString();
    }

    public void setPlazo(String plazo) {
        switch (plazo) {
            case "10D":
                this.plazo = Plazo.D10;
                break;
            case "15D":
                this.plazo = Plazo.D15;
                break;
            case "20D":
                this.plazo = Plazo.D20;
                break;
            case "1M":
                this.plazo = Plazo.M1;
                break;
            case "2M":
                this.plazo = Plazo.M2;
                break;
        }
    }

    public String getCodigoSancion() {
        return codigoSancion;
    }

    public void setCodigoSancion(String codigoSancion) {
        this.codigoSancion = resize(codigoSancion, 20);
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = resize(expediente, 60);
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
        this.articulo = resize(articulo, 80);
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = resize(checkNifPattern(checkNif(limpia(nif))), 15);
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
        this.sancionado = resize(sancionado, 100);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = resize(matricula, 12);
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = resize(cuantia, 15);
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = resize(puntos, 3);
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = resize(localidad, 100);
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = resize(linea, 300);
    }

    public void setFechaVencimiento() {
        LocalDate publicacion = Dates.asLocalDate(this.fechaPublicacion);
        
        switch(this.plazo){
            case D10:
                publicacion.plusDays(10);
                break;
            case D15:
                publicacion.plusDays(15);
                break;
            case D20:
                publicacion.plusDays(20);
                break;
            case M1:
                publicacion.plusMonths(1);
                break;
            case M2:
                publicacion.plusMonths(2);
                break;
        }

        this.fechaVencimiento=Dates.asDate(publicacion);
    }

    public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    private String resize(String aux, int size) {
        if (aux.length() > size) {
            return aux.substring(0, size);
        } else {
            return aux;
        }
    }

    @Override
    public String toString() {
        return "Ins{" + "linea=" + linea + '}';
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
            } else if (cn.isvalido(nif)) {
                aux = nif;
            } else {
                aux = cn.calcular(nif);
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
