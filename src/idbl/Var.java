package idbl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
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
    
    
    private static final Logger log = LogManager.getLogger(Var.class);
    public static Conexion con;
    public static File fileData;
    public static List<Fase> listFases;
    public static List<String> listArt;
    public static boolean isClasificando;
    public static String[][] sqlTask;
    

    public static void init() {
        logger();
        cargaDriverDB();
        ficheros();
        con = new Conexion();
        listFases = new ArrayList();
        listArt = new ArrayList();
        cargaXML();
        cargaVariables();
    }

    private static void logger() {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("log4j2.xml");
        context.setConfigLocation(file.toURI());
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

        sqlTask[0][0] = "CLEAN TEMP";
        sqlTask[0][1] = "DELETE FROM historico.temp_historico WHERE codigoSancion "
                + "IN "
                + "(SELECT codigoSancion FROM historico.sancion)";

        sqlTask[1][0] = "BUILD ORIGEN";
        sqlTask[1][1] = "INSERT INTO historico.origen (nombreOrigen) "
                + "SELECT organismo FROM historico.temp_historico WHERE organismo "
                + "NOT IN "
                + "(SELECT nombreOrigen FROM historico.origen WHERE historico.origen.nombreOrigen = historico.temp_historico.organismo) "
                + "GROUP BY organismo";

        sqlTask[2][0] = "BUILD BOLETIN";
        sqlTask[2][1] = "INSERT INTO historico.boletin (nBoe,origen,fechaPublicacion) "
                + "SELECT a.boe, b.idOrigen, a.fecha_publicacion from historico.temp_historico AS a "
                + "LEFT JOIN historico.origen AS b ON a.organismo=b.nombreOrigen WHERE a.boe "
                + "NOT IN "
                + "(SELECT nBoe FROM historico.boletin WHERE historico.boletin.nBoe = a.boe) "
                + "GROUP BY boe";

        sqlTask[3][0] = "BUILD SANCIONADO";
        sqlTask[3][1] = "INSERT INTO historico.sancionado (nif,tipoJuridico,nombre) "
                + "SELECT cif,tipoJuridico,nombre FROM historico.temp_historico WHERE cif "
                + "NOT IN "
                + "(SELECT cif FROM historico.sancionado WHERE historico.sancionado.nif = historico.temp_historico.cif) "
                + "GROUP BY cif";

        sqlTask[4][0] = "BUILD VEHICULO";
        sqlTask[4][1] = "INSERT INTO historico.vehiculo (matricula) "
                + "SELECT matricula FROM historico.temp_historico WHERE matricula "
                + "NOT IN "
                + "(SELECT matricula FROM historico.vehiculo WHERE historico.vehiculo.matricula = historico.temp_historico.matricula) "
                + "GROUP BY matricula";

        sqlTask[5][0] = "BUILD SANCION";
        sqlTask[5][1] = "INSERT INTO historico.sancion (codigoSancion,expediente,fechaMulta,articulo,cuantia,puntos,nombre,localidad,linea,link) "
                + "SELECT codigoSancion, expediente, fecha_multa, articulo, euros, puntos, nombre, poblacion, linea, link FROM historico.temp_historico";

        sqlTask[6][0] = "BUILD MULTA";
        sqlTask[6][1] = "INSERT INTO historico.multa (idBoletin,idMatricula,idSancionado,idSancion,fase,plazo,fechaEntrada,fechaVencimiento) "
                + "SELECT b.idBoletin,c.idVehiculo,d.idSancionado,e.idSancion,a.fase,a.plazo,CURDATE(),DATE_ADD(a.fecha_publicacion, interval a.plazo day) FROM historico.temp_historico AS a "
                + "LEFT JOIN historico.boletin AS b ON a.boe=b.nBoe "
                + "LEFT JOIN historico.vehiculo AS c ON a.matricula=c.matricula "
                + "LEFT JOIN historico.sancionado AS d ON a.cif=d.nif "
                + "LEFT JOIN historico.sancion AS e ON a.codigoSancion=e.codigoSancion ";

        sqlTask[7][0] = "CLEAN DB";
        sqlTask[7][1] = "DELETE FROM historico.temp_historico";
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
            log.error(io);
        }
    }

    private static void ficheros() {
        fileData = new File("data");
        fileData.mkdirs();
        File aux;
        aux = new File("dsc");
        aux.mkdirs();
    }

}
