package enty;

import java.util.Date;
import util.Dates;

/**
 *
 * @author Agarimo
 */
public class Timming {

    private Date fecha;
    private Date spt;
    private Date car;
    private Date org;
    private Date bol;
    private Date san;
    private Date veh;
    private Date sanc;
    private Date mul;
    private String split;
    private String carga;
    private String organismos;
    private String boletines;
    private String sancionados;
    private String vehiculos;
    private String sanciones;
    private String multas;
    private String total;
    private String status;

    public Timming(Date fecha) {
        this.fecha = fecha;
        split = "";
        organismos = "";
        boletines = "";
        sancionados = "";
        vehiculos = "";
        sanciones = "";
        multas = "";
        total = "";
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setSplit(Date fecha) {
        this.spt = fecha;
        this.split = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.fecha));
    }

    public void setCarga(Date fecha) {
        this.car = fecha;
        this.carga = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.spt));
    }

    public void setOrganismos(Date fecha) {
        this.org = fecha;
        this.organismos = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.car));
    }

    public void setBoletines(Date fecha) {
        this.bol = fecha;
        this.boletines = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.org));
    }

    public void setSancionados(Date fecha) {
        this.san = fecha;
        this.sancionados = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.bol));
    }

    public void setVehiculos(Date fecha) {
        this.veh = fecha;
        this.vehiculos = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.san));
    }

    public void setSanciones(Date fecha) {
        this.sanc = fecha;
        this.sanciones = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.veh));
    }

    public void setMultas(Date fecha) {
        this.mul = fecha;
        this.multas = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.sanc));
    }

    public void setTotal(Date fecha) {
        this.total = Dates.imprimeTiempo(Dates.diferenciaFechas(fecha, this.fecha));
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String SQLCrear() {
        String query = "INSERT into app.timming (fecha,split,carga,organismos,boletines,sancionados,vehiculos,sanciones,multas,total,status) values("
                + util.Varios.entrecomillar(Dates.imprimeFechaCompleta(fecha)) + ","
                + util.Varios.entrecomillar(split) + ","
                + util.Varios.entrecomillar(carga) + ","
                + util.Varios.entrecomillar(organismos) + ","
                + util.Varios.entrecomillar(boletines) + ","
                + util.Varios.entrecomillar(sancionados) + ","
                + util.Varios.entrecomillar(vehiculos) + ","
                + util.Varios.entrecomillar(sanciones) + ","
                + util.Varios.entrecomillar(multas) + ","
                + util.Varios.entrecomillar(total) + ","
                + util.Varios.entrecomillar(status)
                + ");";
        return query;
    }
}
