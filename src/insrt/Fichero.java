package insrt;

import idbl.Var;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Dates;
import util.Files;
import val.Validador;

/**
 *
 * @author Agárimo
 */
public class Fichero extends Thread {

    private final File fichero;
    private final List<File> pila;
    private final List<File> split;
    private final List<File> validador;
    private BB0 archivo;
    private int total;
    private int contador;
    private boolean activo;

    public Fichero(File fichero) {
        activo = true;
        this.fichero = fichero;
        this.pila = new ArrayList<>();
        this.split = new ArrayList<>();
        this.validador = new ArrayList<>();
        llenarSplit(this.fichero);
        if (split.size() > 0) {
            splitPila();
        }
        llenarValidador(this.fichero);
        if (validador.size() > 0) {
            validaPila();
        }
        
        llenarPila(this.fichero);
        Var.tm.setSplit(Dates.curdate());
    }

    @Override
    public void run() {
        while (activo) {
            getStatus();
        }
    }

    public void getStatus() {
//        InsercionC.win.setProgreso(getPorcentaje());
//        InsercionC.win.setTitulo("Completado " + getPorcentaje() + "%");
//        InsercionC.win.setLabel("Cargando datos en la BBDD");
//        InsercionC.win.setProgresoBB0(BB0.getPorcentajeBB0());
    }

    public int getTotal() {
        return total;
    }

    public int getContador() {
        return contador;
    }

    public int getPorcentaje() {
        return (contador * 100) / total;
    }

    public int getTotalBB0() {
        return BB0.getTotalBB0();
    }

    public int getContadorBB0() {
        return BB0.getContadorBB0();
    }

    public int getPorcentajeBB0() {
        return BB0.getPorcentajeBB0();
    }

    private void llenarPila(File fichero) {
        File[] ficheros = fichero.listFiles();

        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                llenarPila(fichero1);
            } else {
                if (fichero1.getName().contains(".bb2")) {
                    pila.add(fichero1);
                } else {
                    Files.moverArchivo(fichero1, new File("dsc", fichero1.getName()));
                }
            }
        }
        total = pila.size();
        contador = 0;
    }

    private void llenarSplit(File fichero) {
        File[] ficheros = fichero.listFiles();

        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                llenarSplit(fichero1);
            } else {
                if (fichero1.getName().contains(".big")) {
                    split.add(fichero1);
                }
            }
        }
    }

    private void llenarValidador(File fichero) {
        File[] ficheros = fichero.listFiles();

        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                llenarValidador(fichero1);
            } else {
                if (fichero1.getName().contains(".bb1")) {
                    validador.add(fichero1);
                }
            }
        }
    }

    private void splitPila() {
//        InsercionC.win.setTitulo("Preparando archivos");
//        InsercionC.win.setLabel("...Spliteando Archivos...");
//        InsercionC.win.setIndeterminado(true);

        File destino = new File("data");
        File aux;
        Iterator it = split.iterator();

        while (it.hasNext()) {
            aux = (File) it.next();
            try {
                Files.splitFile(aux, destino, 50000, "bb1");
                aux.delete();
            } catch (IOException ex) {
                Logger.getLogger(Fichero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        InsercionC.win.setIndeterminado(false);
        pila.clear();

    }

    private void validaPila() {
//        InsercionC.win.setTitulo("Preparando archivos");
//        InsercionC.win.setLabel("...Validando Archivos...");
//        InsercionC.win.setIndeterminado(true);

        Validador val;
        File aux;
        Iterator it = validador.iterator();

        while (it.hasNext()) {
            aux = (File) it.next();
            try {
                val = new Validador(aux);
                aux.delete();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Fichero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
//        InsercionC.win.setIndeterminado(false);
        validador.clear();
    }

    private void procesar() {
        archivo.procesaArchivo();
    }

    public void procesaFichero() {
        Iterator it = pila.iterator();
        while (it.hasNext()) {
            archivo = new BB0((File) it.next());
            procesar();
            contador++;
        }
        activo = false;
    }
}
