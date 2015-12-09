package enty;

import java.util.Date;

/**
 *
 * @author Agarimo
 */
public class MultaS {
    int id;
    Boletin bol;
    Vehiculo veh;
    Sancionado san;
    Sancion sanc;
    String fase;
    int plazo;
    Date fechaEntrada;
    Date fechaVencimiento;
    
    public MultaS(){
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boletin getBol() {
        return bol;
    }

    public void setBol(Boletin bol) {
        this.bol = bol;
    }

    public Vehiculo getVeh() {
        return veh;
    }

    public void setVeh(Vehiculo veh) {
        this.veh = veh;
    }

    public Sancionado getSan() {
        return san;
    }

    public void setSan(Sancionado san) {
        this.san = san;
    }

    public Sancion getSanc() {
        return sanc;
    }

    public void setSanc(Sancion sanc) {
        this.sanc = sanc;
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
    
    
}
