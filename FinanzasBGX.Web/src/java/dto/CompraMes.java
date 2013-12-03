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
    private String mes;
    private double total;

     public CompraMes(String mes) {
        this.mes = mes;
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
}
