/*
 * Copyright (C) 2016 Agarimo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ScriptDoc;

import idbl.Var;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class CheckDoc {

    List<Documento> listado;

    public CheckDoc() {
        listado = Query.listaDocumento("SELECT * FROM idbl.documento limit 500000");
    }

    public void run() {
        int contador = 1;
        int total=listado.size();
        URL url;
        Documento doc;
        File dir;
        File file;
        Iterator<Documento> it = listado.iterator();

        while (it.hasNext()) {
            doc = it.next();

            dir = new File(Var.ftpFileSystem, doc.getFecha());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file = new File(dir, doc.getCodigo() + ".pdf");

            if (!file.exists()) {
                url = doc.getLink();

                if (url != null) {
                    System.out.println("Descargando " + doc.getCodigo());
                    System.out.println("Completado "+ Varios.calculaProgreso(contador, total)+"%");
                    Varios.descargaArchivo(url, file);
                    contador++;
                }
            }else{
                total--;
            }

        }

        System.out.println("Descarga Finalizada");
    }
}
