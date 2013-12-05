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
public class CompraMes implements Serializable {
    private int month;
    private String mes;
    private double total;

     public CompraMes(String mes, int month) {
        this.mes = mes;
        this.month = month;
    }
    
    public CompraMes(String mes, double total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public double getTotal() {
        return total;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
}
