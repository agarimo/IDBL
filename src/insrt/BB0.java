package insrt;

import enty.Temporal;
import idbl.Var;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Log;
import util.Sql;

/**
 *
 * @author Agárimo
 */
public class BB0 {

    private static int contador;
    private static int total;
    public static List<String> lineas;
    List<String>[] listas;
    String nombre;
    String path;
    File destino;
    Temporal temp;

    public BB0(File archivo) {
        this.nombre = archivo.getName();
        this.path = archivo.getParent();
        try {
            lineas = cargaLineas(archivo);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BB0.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BB0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }

    public List getLineas() {
        return BB0.lineas;
    }

    public static int getContadorBB0() {
        return contador;
    }

    public static int getTotalBB0() {
        return total;
    }

    public static int getPorcentajeBB0() {
        if (total == 0) {
            return 0;
        } else {
            return (contador * 100) / total;
        }
    }

    private List<String> cargaLineas(File archivo) throws FileNotFoundException, IOException {
        List<String> aux = new ArrayList<>();
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        while ((linea = br.readLine()) != null) {
            aux.add(linea);
        }
        br.close();
        fr.close();
        total = aux.size();
        contador = 1;
        return aux;
    }

    public void procesaArchivo() {
        try {
            String linea;
            Iterator it = lineas.iterator();
            Sql bd = new Sql(Var.con);
            
            while (it.hasNext()) {
                linea = limpiarLinea((String) it.next());
                temp = split(linea);
                bd.ejecutar(temp.SQLCrear());
                contador++;
                
            }
            bd.close();
        } catch (SQLException ex) {
            Log a= new Log();
            a.escribeError("SQL", ex.getMessage());
            a.escribeError("SQL", temp.SQLCrear());
            Logger.getLogger(BB0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String limpiarLinea(String linea) {
        String str = linea.replace("\'", "\\'");
        str=str.replace("\\", "/");
        return str;
    }

    private Temporal split(String linea) {
        String[] split = linea.split("\\|");
        Temporal aux = new Temporal();

        aux.setCodigoSancion(split[2].trim()+split[8].trim());
        aux.setFechaPublicacion(util.Dates.formatFecha(split[1].trim(), "dd/MM/yyyy"));
        aux.setOrganismo(split[16].trim());
        aux.setBoe(split[2].trim()+split[22].trim());
        aux.setFase(split[3]);
        aux.setTipoJuridico(split[4].trim());
        aux.setPlazo(Integer.parseInt(split[5]));
        aux.setExpediente(split[10].trim());
        aux.setFechaMulta(parseFecha(split[11].trim()));
        aux.setArticulo(split[12].trim());
        aux.setCif(split[15].trim());
        aux.setNombre(split[13].trim());
        aux.setPoblacion("");
        aux.setMatricula(split[14].trim());
        aux.setEuros(split[17].trim());
        aux.setPuntos(split[18].trim());
        aux.setLinea(split[23].trim());
        aux.setLink(split[24].trim());

        return aux;
    }

    private static Date parseFecha(String fecha) {
        Date aux;
        if (fecha.equals("")) {
            return null;
        } else {
            aux = util.Dates.formatFecha(fecha, "ddMMyy");
            if (aux == null) {
                aux = util.Dates.formatFecha(fecha, "MMddyy");
            }
        }
        return aux;
    }
}
