package idbl;

import enty.Stats;
import enty.Timming;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Conexion;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import val.Fase;

/**
 *
 * @author Agarimo
 */
public class Var {

    public static Conexion con;
    public static Timming tm;
    public static Stats st;
    public static List<Fase> listFases;
    public static List<String> listArt;
    public static boolean isClasificando;

    public static void iniciaVariables() {
        driver();
        ficheros();
        con = new Conexion();
        listFases = new ArrayList();
        listArt = new ArrayList();
        cargaXML();
    }

    private static void driver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void ficheros() {
        File aux;
        aux = new File("data");
        aux.mkdirs();
        aux = new File("dsc");
        aux.mkdirs();
    }

    private static void cargaXML() {
        Fase aux;
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("config.xml");

        try {
            Document document = (Document) builder.build(xmlFile);
            Element config = document.getRootElement();
            
            Element conexion = config.getChild("conexion");
            con.setDireccion(conexion.getChildText("db-host"));
            con.setPuerto(conexion.getChildText("db-port"));
            con.setUsuario(conexion.getChildText("db-username"));
            con.setPass(conexion.getChildText("db-password"));
            
            Element fases = config.getChild("fases");
            List list = fases.getChildren();

            for (Iterator it = list.iterator(); it.hasNext();) {
                Element fas = (Element) it.next();
                
                aux = new Fase(fas.getAttributeValue("nombre"));
                aux.setEmpresaCon(fas.getChildText("econ"));
                aux.setEmpresaSin(fas.getChildText("esin"));
                aux.setParticularCon(fas.getChildText("pcon"));
                aux.setParticularSin(fas.getChildText("psin"));

                listFases.add(aux);
            }
        } catch (IOException | JDOMException io) {
            System.out.println(io.getMessage());
        }
    }
}
