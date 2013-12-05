/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author robertogarza
 */
public class VentaMes implements Serializable {
    private int month;
    private int year;
    private String mes;
    private double total;

    public VentaMes() {
    }
    
    public VentaMes(String mes, int month) {
        this.mes = mes;
        this.month = month;
    }
    
    public VentaMes(String mes, double total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public double getTotal() {
        return total;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
    
    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
