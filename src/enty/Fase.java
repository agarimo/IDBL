package enty;

import java.io.Serializable;
import util.Varios;

public class Fase implements Serializable, Comparable<Fase> {

    String id;
    String empresaCon;
    String empresaSin;
    String particularCon;
    String particularSin;

    public Fase(String id) {
        this.id = id;
    }

    public Fase(String id, String empresaC, String empresaS, String particularC, String particularS) {
        this.id = id;
        this.empresaCon = empresaC;
        this.empresaSin = empresaS;
        this.particularCon = particularC;
        this.particularSin = particularS;
    }

    public String getEmpresaCon() {
        return this.empresaCon;
    }

    public void setEmpresaCon(String empresaCon) {
        this.empresaCon = empresaCon;
    }

    public String getEmpresaSin() {
        return this.empresaSin;
    }

    public void setEmpresaSin(String empresaSin) {
        this.empresaSin = empresaSin;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticularCon() {
        return this.particularCon;
    }

    public void setParticularCon(String particularCon) {
        this.particularCon = particularCon;
    }

    public String getParticularSin() {
        return this.particularSin;
    }

    public void setParticularSin(String particularSin) {
        this.particularSin = particularSin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Fase other = (Fase) obj;
        return !(this.id == null ? other.id != null : !this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public int compareTo(Fase o) {
        String a = this.id;
        String b = o.getId();
        return a.compareTo(b);
    }

    public String SQLECon() {
        return "UPDATE idbl.temp_idbl set fase=" + Varios.entrecomillar(this.empresaCon) + " where fase=" + Varios.entrecomillar(this.id) + " and (puntos!='0' and puntos!='') and tipoJuridico='E';";
    }

    public String SQLESin() {
        return "UPDATE idbl.temp_idbl SET fase=" + Varios.entrecomillar(this.empresaSin) + " where fase=" + Varios.entrecomillar(this.id) + " and (puntos='0' or puntos='') and tipoJuridico='E';";
    }

    public String SQLPCon() {
        return "UPDATE idbl.temp_idbl set fase=" + Varios.entrecomillar(this.particularCon) + " where fase=" + Varios.entrecomillar(this.id) + " and (puntos!='0' and puntos!='') and tipoJuridico='P';";
    }

    public String SQLPSin() {
        return "UPDATE idbl.temp_idbl SET fase=" + Varios.entrecomillar(this.particularSin) + " where fase=" + Varios.entrecomillar(this.id) + " and (puntos='0' or puntos='') and tipoJuridico='P';";
    }
}
