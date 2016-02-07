package enty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 *
 * @author Agarimo
 */
public class Doc {

    private String id;
    private String codigo;
    private String fecha;
    private String link;

    public Doc() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public URL getLink() {
        try {
            return new URL(this.link);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String SQLBuscar(){
        return "SELECT id FROM idbl.documento WHERE id="+this.id;
    }
    
    
}
