/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author user
 */
public class NumericTableSet extends TableSet<Double> {
    
    public NumericTreeRow getMaxRow(){
        NumericTreeRow newRow = new NumericTreeRow();
        for(Map.Entry<String, ArrayList<Double>> tableEntry : this.getTable().entrySet()){
            double value = this.getMaxValue(tableEntry.getValue());
            newRow.setValue(tableEntry.getKey(), value);
        }
        return newRow;
    }
    
    public NumericTreeRow getMinRow(){
        NumericTreeRow newRow = new NumericTreeRow();
        for(Map.Entry<String, ArrayList<Double>> tableEntry : this.getTable().entrySet()){
            double value = this.getMinValue(tableEntry.getValue());
            newRow.setValue(tableEntry.getKey(), value);
        }
        return newRow;
    }
    
    public double getMaxValue(ArrayList<Double> values){
        return Collections.max(values);
    }
    
    public double getMinValue(ArrayList<Double> values){
        return Collections.min(values);
    }
    
    public NumericTreeRow getCardinalityRow(){
        NumericTreeRow newRow = new NumericTreeRow();
        for(Map.Entry<String, ArrayList<Double>> tableEntry : this.getTable().entrySet()){
            newRow.setValue(tableEntry.getKey(), (double)tableEntry.getValue().size());
        }
        return newRow;
    }
    
    public NumericTreeRow getMeanRow(){
        NumericTreeRow newRow = new NumericTreeRow();
        for(Map.Entry<String, ArrayList<Double>> tableEntry : this.getTable().entrySet()){
            newRow.setValue(tableEntry.getKey(), this.getMean(tableEntry.getValue()));
        }
        return newRow;
    }
    
    public NumericTreeRow getSumRow(){
        NumericTreeRow newRow = new NumericTreeRow();
        for(Map.Entry<String, ArrayList<Double>> tableEntry : this.getTable().entrySet()){
            newRow.setValue(tableEntry.getKey(), this.getSum(tableEntry.getValue()));
        }
        return newRow;
    }
    
    public double getMean(ArrayList<Double> rows){
        double sum = 0;
        for(Double row : rows){
            sum = sum + row.doubleValue();
        }
        return (double)sum / (double)rows.size();
    }
    
    public double getSum(ArrayList<Double> rows){
        double sum = 0;
        for(Double row : rows){
            sum = sum + row.doubleValue();
        }
        return (double)sum;
    }
    
}
