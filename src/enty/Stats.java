package enty;

import java.util.Date;
import util.Dates;

/**
 *
 * @author Agarimo
 */
public class Stats {

    private Date fecha;
    private int bol;
    private int san;
    private int bol_err;
    private int san_err;
    private int bol_ini;
    private int san_ini;

    public Stats(Date fecha, int bol_ini, int san_ini) {
        this.fecha = fecha;
        this.bol_ini = bol_ini;
        this.san_ini = san_ini;
    }

    public Stats(Date fecha, int bol, int san, int bol_err, int san_err) {
        this.fecha = fecha;
        this.bol = bol;
        this.san = san;
        this.bol_err = bol_err;
        this.san_err = san_err;
        
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setBol(int bol) {
        this.bol = bol-this.bol_ini;
    }

    public void setSan(int san) {
        this.san = san-this.san_ini;
    }

    public void setBol_err(int bol_err) {
        this.bol_err = bol_err;
    }

    public void setSan_err(int san_err) {
        this.san_err = san_err;
    }
    
    public int getSan_err(){
        return this.san_err-this.san;
    }
    
    public String SQLCrear() {
        String query = "INSERT into app.stats (fecha,bol,san,bol_err,san_err) values("
                + util.Varios.entrecomillar(Dates.imprimeFechaCompleta(fecha)) + ","
                + this.bol + ","
                + this.san + ","
                + this.bol_err+ ","
                + getSan_err()
                + ");";
        return query;
    }

}
