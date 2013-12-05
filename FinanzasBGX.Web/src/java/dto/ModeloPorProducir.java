/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author robertogarza
 */
public class ModeloPorProducir {
    private String modelo;
    private double total;

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTotal() {
        return total;
    }
    
}
