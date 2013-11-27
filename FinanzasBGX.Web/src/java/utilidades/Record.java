/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.Serializable;

/**

 @author user
 */
public class Record implements Serializable{
    public Integer id;
    public String name;
    public Double value;
    
    public Record(Integer id, String name, Double value){
        this.id = id;
        this.name = name;
        this.value = value;
    }
    
}
