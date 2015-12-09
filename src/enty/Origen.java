package enty;


/**
 *
 * @author Agárimo
 */
public class Origen {

    int idOrigen;
    String nombre;
    
    
    public Origen (String nombre){
        this.nombre=nombre;
    }
    
    public Origen (int idOrigen, String nombre){
        this(nombre);
        this.idOrigen=idOrigen;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.idOrigen;
        hash = 67 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
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
        final Origen other = (Origen) obj;
        if (this.idOrigen != other.idOrigen) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Origen{" + "idOrigen=" + idOrigen + ", nombre=" + nombre + '}';
    }
    
    
    public String crearOrigen(){
        String query="INSERT into historico.origen (nombreOrigen) values("
                + util.Varios.entrecomillar(this.nombre)
                + ");";
        return query;
    }
    
    public String buscar(){
        String query="SELECT idOrigen FROM historico.origen where nombreOrigen="+util.Varios.entrecomillar(this.nombre);
        return query;
    }
}

