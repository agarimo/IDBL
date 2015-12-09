package enty;

import util.Varios;

/**
 *
 * @author Agárimo
 */
public class Sancionado {

    int id;
    String nif;
    String tipoJuridico;
    String nombre;

    public Sancionado(int id) {
        this.id = id;
    }

    public Sancionado(String nif, String tipoJuridico, String nombre) {
        this.nif = nif;
        this.tipoJuridico = tipoJuridico;
        this.nombre = nombre;
    }

    public Sancionado(int id, String nif, String tipoJuridico, String nombre) {
        this.id = id;
        this.nif = nif;
        this.tipoJuridico = tipoJuridico;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getTipoJuridico() {
        return tipoJuridico;
    }

    public void setTipoJuridico(String tipoJuridico) {
        this.tipoJuridico = tipoJuridico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String SQLCrear() {
        String query = "INSERT into historico.sancionado (nif,nombre,tipoJuridico,nombre) values("
                + util.Varios.entrecomillar(nif) + ","
                + util.Varios.entrecomillar(tipoJuridico) + ","
                + util.Varios.entrecomillar(nombre)
                + ");";

        return query;
    }

    public String SQLBuscar() {
        String query = "SELECT idSancionado FROM historico.sancionado WHERE nif=" + util.Varios.entrecomillar(nif) + ";";

        return query;
    }

    public String SQLEditarNombre() {
        String query = "UPDATE historico.sancionado SET "
                + "nombre=" + Varios.entrecomillar(nombre) + " "
                + "WHERE idSancionado=" + id;
        return query;
    }
    
    public String SQLEditarNif(){
        String query = "UPDATE historico.sancionado SET "
                + "nif=" + Varios.entrecomillar(nif) + " "
                + "WHERE idSancionado=" + id;
        return query;
    }

    @Override
    public String toString() {
        return "Sancionado{" + "id=" + id + ", nif=" + nif + ", tipoJuridico=" + tipoJuridico + '}';
    }
}
