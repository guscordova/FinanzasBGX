/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

/**

 @author user
 */
public class DateUtils {
    public static String getMonth(int month){
        String[] months = {"enero", "febrero", "marzo", "abril", "mayo", 
                         "junio", "julio", "agosto", "septiembre", "octubre",
                         "noviembre", "diciembre"};
        return months[month];
    }
    
    public static NumericTableSet getDatedTableSet(){
        NumericTableSet ts = new NumericTableSet();
        for(int i = 0; i < 12; i++){
            ts.addValue(DateUtils.getMonth(i), (double)0);
        }
        return ts;
    }
}
