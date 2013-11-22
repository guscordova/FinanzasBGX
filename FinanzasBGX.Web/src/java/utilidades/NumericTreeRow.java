/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author user
 */
public class NumericTreeRow extends Row<Double>{
    private TreeSet<Column> tree;
    
    public NumericTreeRow(){
        super();
        this.tree = new TreeSet<>();
    }
    
    public void substract(NumericTreeRow tr){
        for(String col : tr.getColumns()){
            double myval = this.getValue(col);
            double trval = tr.getValue(col);
            this.setValue(col, myval - trval);
        }
    }
    
    public double getMaxValue(){
        double maxValue = -1;
        for(String column : this.getColumns()){
            double value = this.getValue(column);
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue;
    }

    public ArrayList<Column> getDescendingColumns(){
        TreeSet<Column> invTree = (TreeSet<Column>)this.tree.descendingSet();
        ArrayList<Column> columns = new ArrayList<>();
        for(Column col : invTree){
            columns.add(col);
        }
        return columns;
    }
    
    public ArrayList<Column> getAscendingColumns(){
        ArrayList<Column> columns = new ArrayList<>();
        for(Column col : this.tree){
            columns.add(col);
        }
        return columns;
    }
    
    @Override
    public void setValue(String column, Double value){
        super.setValue(column, value);
        Column col = new Column(column, value);
        this.tree.add(col);
    }
    
    @Override
    public String toString(){
        StringWriter writer = new StringWriter();
        TreeSet<Column> invTree = (TreeSet<Column>)this.tree.descendingSet();
        for(Column col : invTree){
            writer.write(col.name + " ");
            writer.write(col.value + "\n");
        }
        return writer.toString();
    }
    
}
