package enty;

import java.util.Date;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Carga {

    private final Date inicio;
    private Date fin;
    private int multas;
    private int documentos;
    private String total;
    private String status;

    public Carga() {
        inicio = Dates.curdate();
    }

    public void setMultas(int multas) {
        this.multas = multas;
    }

    public void setDocumentos(int documentos) {
        this.documentos = documentos;
    }
    
    public void setStatus (String status){
        this.status=status;
    }

    public void setFin() {
        fin = Dates.curdate();
        total = Dates.imprimeTiempo(Dates.diferenciaFechas(fin, inicio));
    }

    public String SQLCrear() {
        return "INSERT INTO idbl_stats.carga (fecha, multas, documentos, inicio, fin, total,status) values("
                + "CURDATE(),"
                + this.multas + ","
                + this.documentos + ","
                + Varios.entrecomillar(Dates.imprimeFechaCompleta(this.inicio)) + ","
                + Varios.entrecomillar(Dates.imprimeFechaCompleta(this.fin)) + ","
                + Varios.entrecomillar(this.total) + ","
                + Varios.entrecomillar(this.status)
                + ");";
    }
}
