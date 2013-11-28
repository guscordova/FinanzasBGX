/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class Column implements Comparable<Column>,  Serializable {

    public String name;
    public Double value;

    public Column(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Column o) {
        return ( this.value < o.value) ? -1 : 1;
    }
}
