/*
 * The MIT License
 *
 * Copyright 2016 Agarimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ScriptDoc;

import idbl.Var;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class Query {

    public static Sql bd;


    public static Documento getDocumento(String id) {
        ResultSet rs;
        Documento aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs("SELECT * FROM idbl.documento WHERE id=" + id);

            if (rs.next()) {
                aux = new Documento();
                aux.setId(rs.getInt("id"));
                aux.setFecha(rs.getString("fecha"));
                aux.setCodigo(rs.getString("codigo"));
            }

            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

     public static List<Documento> listaDocumento(String query) {
        List<Documento> list = new ArrayList();
        ResultSet rs;
        Documento aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Documento();
                aux.setId(rs.getInt("id"));
                aux.setFecha(rs.getString("fecha"));
                aux.setCodigo(rs.getString("codigo"));
                
                list.add(aux);
            }

            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
}
