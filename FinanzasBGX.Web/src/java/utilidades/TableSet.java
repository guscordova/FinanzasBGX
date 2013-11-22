/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author user
 */
public class TableSet<T> implements Serializable{
    String name;
    HashMap<String, ArrayList<T>> table;
    
    public TableSet(){
        this.table = new HashMap<>();
    }
    
    public HashMap<String, ArrayList<T>> getTable(){
        return this.table;
    }
    
    public Set<String> getColumns(){
        return this.table.keySet();
    }
    
    public int getNColumns(){
        return this.table.keySet().size();
    }
    
    public ArrayList<T> getRows(String column){
        return this.table.get(column);
    }
    
    @Override
    public String toString(){
        StringWriter writer = new StringWriter();
        for(String column : this.getColumns()){
            writer.write(column + " ");
            ArrayList<T> values = this.getRows(column);
            for(T value : values){
                writer.write(value.toString() + " ");
            }
            writer.write("\n");
        }
        return writer.toString();
    }
    
    public void addRow(Row<T> row){
        for(String column : row.getColumns()){
            this.addValue(column, row.getValue(column));
        }
    }
    
    public void addValues(String column, ArrayList<T> values){
        for(T value : values){
            this.addValue(column, value);
        }
    }
    
    public void addValue(String column, T value){
        ArrayList<T> rows;
        //  Check if column exist
        if(!this.table.containsKey(column)){
            //  If doesnt exist, add it
            rows = new ArrayList<>();
            this.table.put(column, rows);
        }
        else{
            //  get the rows
            rows = this.table.get(column);
        }
        //  Add the new value
        rows.add(value);
    }
    
    public T getValue(String column, int row){
        ArrayList<T> rows = this.table.get(column);
        return rows.get(row);
    }
}