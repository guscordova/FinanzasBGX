/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilidades;

import entities.Cliente;
import entities.Compra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Henry
 */
public class compraUtils {
    private ArrayList<Compra> Compras;
     
     public compraUtils(List<Compra> compras){
        Compras = new ArrayList<>();
        Compras.addAll(compras);
    }
     
    public double getYearPurchases(int year){
        double acum = 0;
        for(Compra c:Compras){
            if(c.getFecPago().getYear() == year){
                acum +=c.getCostoTotal();
            }
        }
     return acum;
    }
    
    public double[] getMonthPurchases(int year){
        double[] acum = new double[12];
        for(Compra c:Compras){
            if(c.getFecPago().getYear() == year){
                acum[c.getFecPago().getMonth()] += c.getCostoTotal();
            }
        }
     return acum;
    }
    
    public HashMap<Integer, Double> getSupplierPurchases(int year){
        HashMap<Integer, Double> proveedorAcum = new HashMap<>();
        for(Compra c:Compras){
            if(c.getFecPago().getYear() == year){
                Integer key = c.getProveedorId().getId();
                double value;
                if(!proveedorAcum.containsKey(key)){
                    value = 0;
                    proveedorAcum.put(key, value);
                }
                else{
                    value = proveedorAcum.get(key);
                }
                value += c.getCostoTotal();
                proveedorAcum.put(key, value);
            }
        }
     return proveedorAcum;
    }
}
