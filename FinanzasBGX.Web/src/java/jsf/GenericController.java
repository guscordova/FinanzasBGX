/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("genericController")
@SessionScoped
public class GenericController implements Serializable {
    private String currentYear;
    private String currentMonth;
    
    @PostConstruct
    public void init() {
        Calendar today = Calendar.getInstance();
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        currentYear = String.valueOf(today.get(Calendar.YEAR));
        currentMonth = monthNames[today.get(Calendar.MONTH)];
        
    }
    
    public String getCurrentYear() {
        return currentYear;
    }
    
    public String getCurrentMonth() {
        return currentMonth;
    }
}
