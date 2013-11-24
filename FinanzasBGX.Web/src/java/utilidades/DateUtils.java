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
}
