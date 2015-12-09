package insrt;

import enty.Stats;
import enty.Timming;
import idbl.Var;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Dates;
import util.Log;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class SqlTask {

    Sql bd;

    public SqlTask() {
        iniciaStats();
        procesaTemporal();
    }

    private void iniciaStats() {
        int bol, san;
//        InsercionC.win.setIndeterminado(true);
//        InsercionC.win.setLabel("Preparando Datos");

        try {
            bd = new Sql(Var.con);
            bol = 0;
            san = 0;
            bd.close();

            Var.st = new Stats(Dates.curdate(), bol, san);
        } catch (SQLException ex) {
            Logger.getLogger(SqlTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void procesaTemporal() {
        try {
            Var.tm.setCarga(Dates.curdate());
//            InsercionC.win.setIndeterminado(true);
//            InsercionC.win.setLabel("Preparando Datos");
            limpiezaDatos();
//            InsercionC.win.setLabel("Procesando Organismos");
            procesaOrganismos();
            Var.tm.setOrganismos(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 16%");
//            InsercionC.win.setLabel("Procesando Boletines");
            procesaBoletines();
            Var.tm.setBoletines(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 32%");
//            InsercionC.win.setLabel("Procesando Sancionados");
            procesaSancionados();
            Var.tm.setSancionados(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 48%");
//            InsercionC.win.setLabel("Procesando Vehiculos");
            procesaVehiculos();
            Var.tm.setVehiculos(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 64%");
//            InsercionC.win.setLabel("Procesando Sanciones");
            procesaSanciones();
            Var.tm.setSanciones(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 80%");
//            InsercionC.win.setLabel("Procesando Multas");
            procesaMultas();
            Var.tm.setMultas(Dates.curdate());
//            InsercionC.win.setTitulo("Completado 100%");
//            InsercionC.win.setLabel("Limpiando BBDD");
            limpiar();
//            InsercionC.win.setFin();
            Var.tm.setTotal(Dates.curdate());
            procesaTimmingStats(Var.tm, "OK");
            Var.tm = null;

        } catch (SQLException ex) {
            try {
                procesaTimmingStats(Var.tm, "ERROR");
                limpiar();
            } catch (SQLException ex1) {
                Logger.getLogger(SqlTask.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Log lg = new Log();
            lg.escribeError("SQL", ex.getMessage());
            Logger.getLogger(SqlTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiezaDatos() throws SQLException {
        String query = "Delete from historico.temp_historico where codigoSancion in (Select codigoSancion from historico.sancion)";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        bd.close();
    }

    private void procesaOrganismos() throws SQLException {
        String query = "insert into historico.origen (nombreOrigen) select organismo from historico.temp_historico where organismo not in "
                + "(select nombreOrigen from historico.origen where historico.origen.nombreOrigen = historico.temp_historico.organismo) group by organismo";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        bd.close();
    }

    private void procesaBoletines() throws SQLException {
        String query = "insert into historico.boletin (nBoe,origen,fechaPublicacion) select a.boe, b.idOrigen, a.fecha_publicacion from historico.temp_historico as a "
                + "left join historico.origen as b on a.organismo=b.nombreOrigen where a.boe not in "
                + "(select nBoe from historico.boletin where historico.boletin.nBoe = a.boe) group by boe";

        bd = new Sql(Var.con);
        Var.st.setBol_err(0);
        Var.st.setSan_err(0);
        bd.ejecutar(query);
        Var.st.setBol(0);
        bd.close();
    }

    private void procesaSancionados() throws SQLException {
        String query = "insert into historico.sancionado (nif,tipoJuridico,nombre) select cif,tipoJuridico,nombre from historico.temp_historico where cif not in "
                + "(select cif from historico.sancionado where historico.sancionado.nif = historico.temp_historico.cif) group by cif";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        bd.close();
    }

    private void procesaVehiculos() throws SQLException {
        String query = "insert into historico.vehiculo (matricula) select matricula from historico.temp_historico where matricula not in "
                + "(select matricula from historico.vehiculo where historico.vehiculo.matricula = historico.temp_historico.matricula) group by matricula";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        bd.close();
    }

    private void procesaSanciones() throws SQLException {
        String query = "insert into historico.sancion (codigoSancion,expediente,fechaMulta,articulo,cuantia,puntos,nombre,localidad,linea,link) "
                + "select codigoSancion, expediente, fecha_multa, articulo, euros, puntos, nombre, poblacion, linea, link from historico.temp_historico";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        Var.st.setSan(0);
        bd.close();
    }

    private void procesaMultas() throws SQLException {
        String query = "insert into historico.multa (idBoletin,idMatricula,idSancionado,idSancion,fase,plazo,fechaEntrada,fechaVencimiento) "
                + "select b.idBoletin,c.idVehiculo,d.idSancionado,e.idSancion,a.fase,a.plazo,CURDATE(),DATE_ADD(a.fecha_publicacion, interval a.plazo day) from historico.temp_historico as a "
                + "Left join historico.boletin as b on a.boe=b.nBoe "
                + "Left join historico.vehiculo as c on a.matricula=c.matricula "
                + "Left join historico.sancionado as d on a.cif=d.nif "
                + "Left join historico.sancion as e on a.codigoSancion=e.codigoSancion ";
        bd = new Sql(Var.con);
        bd.ejecutar(query);
        bd.close();
    }

    private void limpiar() throws SQLException {
        bd = new Sql(Var.con);
        bd.ejecutar("delete from historico.temp_historico");
        bd.close();
    }

    private void procesaTimmingStats(Timming aux, String ex) throws SQLException {
        Var.tm.setStatus(ex);
        bd = new Sql(Var.con);
        bd.ejecutar(aux.SQLCrear());
        bd.close();
    }
}
