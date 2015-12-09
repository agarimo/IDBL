package enty;


/**
 *
 * @author Agárimo
 */
public class Vehiculo {
    int id;
    String matricula;
    
    public Vehiculo(int id){
        this.id=id;
    }
    
    public Vehiculo(String matricula){
        this.matricula=matricula;
    }
    
    public Vehiculo(int id,String matricula){
        this.id=id;
        this.matricula=matricula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public String crearVehiculo(){
        String query="INSERT into historico.vehiculo (matricula) values("
                +util.Varios.entrecomillar(matricula)
                +");";
        
        return query;
    }
    
    public String buscarVehiculo(){
        String query="SELECT idVehiculo FROM historico.vehiculo WHERE matricula="+util.Varios.entrecomillar(matricula)+";";

        return query;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", matricula=" + matricula + '}';
    }
    
    
}
