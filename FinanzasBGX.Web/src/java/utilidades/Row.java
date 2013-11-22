/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author user
 */
public class Row<T> implements Serializable{
    private HashMap<String, T> row;
    private int resumeCount;
    
    public Row(){
        this.row = new HashMap<>();
        this.resumeCount = 0;
    }
    
    public int getResumeCount(){
        return this.resumeCount;
    }
    
    public void setResumeCount(int rc){
        this.resumeCount = rc;
    }
    
    public int getNColumns(){
        return this.row.keySet().size();
    }
    
    public void deleteColumn(String name){
        this.row.remove(name);
    }
    
    public Set<String> getColumns(){
        return this.row.keySet();
    }
    
    public void setValue(String column, T value){
        this.row.put(column, value);
    }
    
    public boolean containsColumn(String column){
        return this.row.containsKey(column);
    }
    
    public boolean containsValue(T value){
        return this.row.containsValue(value);
    }
    
    public T getValue(String column){
        return this.row.get(column);
    }
    
    @Override
    public String toString(){
        StringWriter writer = new StringWriter();
        for(String column : this.getColumns()){
            writer.write(column + " ");
            T value = this.getValue(column);
            writer.write(value.toString() + " ");
            writer.write("\n");
        }
        return writer.toString();
    }
}

