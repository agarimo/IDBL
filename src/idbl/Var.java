package idbl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import util.Conexion;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import enty.Fase;

/**
 *
 * @author Agarimo
 */
public class Var {

    private static final Logger log = LogManager.getLogger(Var.class);
    public static Conexion con;
    public static File fileData;
    public static File dscData;
    public static List<Fase> listFases;
    public static String[][] sqlTask;

    public static void init() {
        cargaDriverDB();
        ficheros();
        cargaXML();
        cargaVariables();
    }

    private static void cargaDriverDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            log.fatal(ex);
        }
    }

    private static void cargaVariables() {
        sqlTask = new String[8][8];

        sqlTask[0][0] = "CLEAN DUPLICATES";
        sqlTask[0][1] = "DELETE FROM idbl.temp_idbl WHERE codigoSancion "
                + "IN "
                + "(SELECT codigoSancion FROM idbl.sancion)";

        sqlTask[1][0] = "BUILD ORIGEN";
        sqlTask[1][1] = "INSERT INTO idbl.origen (nombreOrigen) "
                + "SELECT organismo FROM idbl.temp_idbl WHERE organismo "
                + "NOT IN "
                + "(SELECT nombreOrigen FROM idbl.origen WHERE idbl.origen.nombreOrigen = idbl.temp_idbl.organismo) "
                + "GROUP BY organismo";

        sqlTask[2][0] = "BUILD BOLETIN";
        sqlTask[2][1] = "INSERT INTO idbl.boletin (nBoe,origen,fechaPublicacion) "
                + "SELECT a.boe, b.idOrigen, a.fecha_publicacion from idbl.temp_idbl AS a "
                + "LEFT JOIN idbl.origen AS b ON a.organismo=b.nombreOrigen WHERE a.boe "
                + "NOT IN "
                + "(SELECT nBoe FROM idbl.boletin WHERE idbl.boletin.nBoe = a.boe) "
                + "GROUP BY boe";

        sqlTask[3][0] = "BUILD SANCIONADO";
        sqlTask[3][1] = "INSERT INTO idbl.sancionado (nif,tipoJuridico,nombre) "
                + "SELECT cif,tipoJuridico,nombre FROM idbl.temp_idbl WHERE cif "
                + "NOT IN "
                + "(SELECT cif FROM idbl.sancionado WHERE idbl.sancionado.nif = idbl.temp_idbl.cif) "
                + "GROUP BY cif";

        sqlTask[4][0] = "BUILD VEHICULO";
        sqlTask[4][1] = "INSERT INTO idbl.vehiculo (matricula) "
                + "SELECT matricula FROM idbl.temp_idbl WHERE matricula "
                + "NOT IN "
                + "(SELECT matricula FROM idbl.vehiculo WHERE idbl.vehiculo.matricula = idbl.temp_idbl.matricula) "
                + "GROUP BY matricula";

        sqlTask[5][0] = "BUILD SANCION";
        sqlTask[5][1] = "INSERT INTO idbl.sancion (codigoSancion,expediente,fechaMulta,articulo,cuantia,puntos,nombre,localidad,linea) "
                + "SELECT codigoSancion, expediente, fecha_multa, articulo, euros, puntos, nombre, poblacion, linea FROM idbl.temp_idbl";

        sqlTask[6][0] = "BUILD MULTA";
        sqlTask[6][1] = "INSERT INTO idbl.multa (idBoletin,idMatricula,idSancionado,idSancion,fase,plazo,fechaEntrada,fechaVencimiento) "
                + "SELECT b.idBoletin,c.idVehiculo,d.idSancionado,e.idSancion,a.fase,a.plazo,CURDATE(),DATE_ADD(a.fecha_publicacion, interval a.plazo day) FROM idbl.temp_idbl AS a "
                + "LEFT JOIN idbl.boletin AS b ON a.boe=b.nBoe "
                + "LEFT JOIN idbl.vehiculo AS c ON a.matricula=c.matricula "
                + "LEFT JOIN idbl.sancionado AS d ON a.cif=d.nif "
                + "LEFT JOIN idbl.sancion AS e ON a.codigoSancion=e.codigoSancion ";

        sqlTask[7][0] = "CLEAN DB";
        sqlTask[7][1] = "DELETE FROM idbl.temp_idbl";
    }

    private static void cargaXML() {
        con = new Conexion();
        listFases = new ArrayList();
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
            log.error(io);
        }
    }

    private static void ficheros() {
        fileData = new File("data");
        fileData.mkdirs();
        dscData = new File("dsc");
        dscData.mkdirs();
    }
}
