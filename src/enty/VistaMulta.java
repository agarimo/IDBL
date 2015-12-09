package enty;

import java.util.Date;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class VistaMulta {
    String sancion;
    String fase;
    Date publicacion;
    Date vencimiento;
    boolean plazo;
    
    
    public VistaMulta(){
        
    }

    public VistaMulta(String sancion, String fase, Date publicacion, Date vencimiento) {
        this.sancion = sancion;
        this.fase = fase;
        this.publicacion = publicacion;
        this.vencimiento = vencimiento;
        this.plazo=setIsPlazo();
    }

    public String getSancion() {
        return sancion;
    }

    public void setSancion(String sancion) {
        this.sancion = sancion;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Date getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Date publicacion) {
        this.publicacion = publicacion;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public boolean isPlazo() {
        return plazo;
    }

    public void setPlazo(boolean plazo) {
        this.plazo = plazo;
    }
    
    private boolean setIsPlazo(){
        Date fecha=Dates.curdate();
        
        return !publicacion.before(fecha);
    }
    
    public static String SQLBuscarNif(String aux){
        return "SELECT a.idSancion,a.fase,b.fechaPublicacion,a.fechaVencimiento FROM historico.multa as a left join historico.boletin as b on a.idBoletin=b.idBoletin "
                + "WHERE a.idSancionado in (select idSancionado from historico.sancionado where nif="+Varios.entrecomillar(aux)+");";
    }
    
    public static String SQLBuscarMatricula(String aux){
        return "SELECT a.idSancion,a.fase,b.fechaPublicacion,a.fechaVencimiento FROM historico.multa as a left join historico.boletin as b on a.idBoletin=b.idBoletin "
                + "WHERE a.idMatricula in (select idVehiculo from historico.vehiculo where matricula="+Varios.entrecomillar(aux)+");";
    }
    
    public static String SQLBuscarExpediente(String aux){
        return "SELECT a.idSancion,a.fase,b.fechaPublicacion,a.fechaVencimiento FROM historico.multa as a left join historico.boletin as b on a.idBoletin=b.idBoletin "
                + "WHERE a.idSancion in (select idSancion from historico.sancion where expediente="+Varios.entrecomillar(aux)+");";
    }
    
    public static String SQLBuscarNifA(String aux){
        return "SELECT nif from historico.sancionado where nif like"+Varios.entrecomillar("%"+aux+"%")+" limit 100;";
    }
    
    public static String SQLBuscarMatriculaA(String aux){
        return "SELECT matricula from historico.vehiculo where matricula like "+Varios.entrecomillar("%"+aux+"%")+" limit 100;";
    }
    
    public static String SQLBuscarExpedienteA(String aux){
        return "SELECT expediente from historico.sancion where expediente like "+Varios.entrecomillar("%"+aux+"%")+" limit 100;";
    }
    
}
