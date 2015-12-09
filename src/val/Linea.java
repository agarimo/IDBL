package val;

import java.sql.SQLException;
import idbl.Var;
import util.CalculaNif;

public final class Linea {

    String linea;
    String[] componentes;
    String fase;
    String tipoJuridico;
    String puntos;
    String articulo;
    String nif;
    String nombre;
    boolean testra = false;
    String cadenaCif = "ABCDEFGHJKLMNPQRSUVW";
    String cadenaNie = "XYZ";
    CalculaNif cal;

    public Linea(String linea) throws SQLException {
        cal = new CalculaNif();
        this.linea = linea;
        this.componentes = linea.split("\\|");
        limpiaComponentes();

        if (this.componentes[0].equals("00001")) {
            this.testra = true;
        }

        this.fase = this.componentes[3];
        this.tipoJuridico = this.componentes[4];
        this.articulo = this.componentes[12];
        this.nif = this.componentes[15].trim();
        this.puntos = this.componentes[18].trim();
        this.nombre = this.componentes[13];

        setPuntos();
        setFase();
        setNif();
    }

    private void limpiaComponentes() {
        for (int i = 0; i < this.componentes.length; i++) {
            this.componentes[i] = this.componentes[i].replace("'", "´");
        }
    }

    private void setPuntos() {
        try {
            int punt = Integer.parseInt(this.puntos);

            if (punt < 1) {
                this.componentes[18] = "0  ";
            }
        } catch (Exception e) {
            this.componentes[18] = "N.P";
        }
    }

    private void setNif() {
        if (this.nif.equals("")) {
            this.nif = "NO*CONSTA ";
            this.componentes[15] = this.nif;
        } else {
            switchNif();
            this.componentes[15] = this.nif;
        }
    }

    private void switchNif() {
        try {
            if (this.cadenaCif.contains("" + this.nif.charAt(0))) {
                setCif(this.nif);
            } else if (this.cadenaNie.contains("" + this.nif.charAt(0))) {
                setNie(this.nif);
            } else {
                setDni(this.nif);
            }
        } catch (Exception e) {
            this.componentes[15] = this.nif;
        }
    }

    private void setCif(String str) {
        str = str.trim();

        if (str.length() == 8) {
            str = cal.calcular(str);
        }

        str = completaEspacios(str);
        if (!this.testra) {
//            this.tipoJuridico = "E";
        }
        this.nif = str;
    }

    private void setDni(String str) {

        if (str.length() <= 8) {
            str = cal.calcular(str);
        }

        if (!this.testra) {
//            this.tipoJuridico = "P";
        }
        str = completaEspacios(str);
        this.nif = str;
    }

    private void setNie(String str) {

        if (str.length() == 8) {
            str = cal.calcular(str);
        }

        if (!this.testra) {
//            this.tipoJuridico = "P";
        }
        this.nif = str;
    }

    private String completaEspacios(String str) {
        while (str.length() < 10) {
            str = str + " ";
        }
        return str;
    }

    private void setFase() {
        Fase aux = new Fase(this.fase);
        int i = Var.listFases.indexOf(aux);

        if (i != -1) {
            aux = (Fase) Var.listFases.get(i);
            editaFase(aux);
        }
    }

    private void editaFase(Fase aux) {
        String str;
        if (getTipoSancionado()) {
            if (getPuntos()) {
                str = aux.getEmpresaCon();
            } else {
                str = aux.getEmpresaSin();
            }
        } else {
            if (getPuntos()) {
                str = aux.getParticularCon();
            } else {
                str = aux.getParticularSin();
            }
        }
        if ((!this.testra)
                && (getArticulo())) {
            str = "717";
        }

        this.componentes[3] = str;
    }

    private boolean getTipoSancionado() {
        this.componentes[4] = this.tipoJuridico;

        return this.tipoJuridico.equalsIgnoreCase("E");
    }

    private boolean getPuntos() {
        try {
            this.puntos = this.puntos.trim();
            int punt = Integer.parseInt(this.puntos);

            return punt > 1;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getArticulo() {
        boolean a = false;

        for (String aux : Var.listArt) {
            if (this.articulo.toUpperCase().contains(aux)) {
                a = true;
            }
        }
        return a;
    }

    private String recomponerLinea() {
        String str = this.componentes[0];

        for (int i = 1; i < this.componentes.length; i++) {
            str = str + "|" + this.componentes[i];
        }
        return str;
    }

    @Override
    public String toString() {
        return recomponerLinea();
    }
}
