/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilidades;

import entities.Cliente;
import entities.Orden;
import entities.Venta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Henry
 */
public class ventaUtils {
     private ArrayList<Venta> Ventas;
     private ArrayList<Orden> Ordenes;
     
     public ventaUtils(List<Venta> ventas){
        Ventas = new ArrayList<>();
        Ventas.addAll(ventas);
    }
     
    public ventaUtils(List<Venta> ventas, List<Orden> ordenes){
        this(ventas);
        Ordenes = new ArrayList<>();
        Ordenes.addAll(ordenes);
    }
     
    public double getYearSales(int year){
        double acum = 0;
        for(Venta v:Ventas){
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if(cal.get(Calendar.YEAR) == year){
                acum += v.getMonto() * v.getCantidad();
            }
        }
     return acum;
    }
    
    public double[] getMonthSales(int year){
        double[] acum = new double[12];
        for(Venta v:Ventas){
            if(v.getFecCobro().getYear() == year){
                acum[v.getFecCobro().getMonth()] += v.getMonto() * v.getCantidad();
            }
        }
     return acum;
    }
    
    public HashMap<Integer, Double> getDistributorSales(int year){
        HashMap<Integer, Double> distribuidorAcum = new HashMap<>();
        for(Venta v:Ventas){
            if(v.getFecCobro().getYear() == year){
                Integer key = v.getOrdenId().getClienteId().getId();
                double value;
                if(!distribuidorAcum.containsKey(key)){
                    value = 0;
                    distribuidorAcum.put(key, value);
                }
                else{
                    value = distribuidorAcum.get(key);
                }
                value += v.getCantidad()*v.getMonto();
                distribuidorAcum.put(key, value);
            }
        }
     return distribuidorAcum;
    }
    
    public double getPendienteCobrarAnual(int year){
        double acum = 0;
        for(Orden o:Ordenes){
           if(o.getFecAlta().getYear() == year){
                acum +=o.getTotalPago();
           } 
        }
    return acum - this.getYearSales(year);
    }
    
    public double[] getPendienteCobrarMensual(int year){
        double[] acum = new double[12];
        for(Orden o:Ordenes){
           if(o.getFecAlta().getYear() == year){
                acum[o.getFecAlta().getMonth()] += o.getTotalPago();
            }
        }
        double[] acum2 = this.getMonthSales(year);
        for(int i=0;i < acum.length; i++){
            acum[i] = acum[i] - acum2[i];
        }
    return acum;
    }
    
    public HashMap<Integer, Double> getPendienteCobrarDistribuidor(int year){
       HashMap<Integer, Double> distribuidorAcum = new HashMap<>();
        for(Orden o:Ordenes){
            if(o.getFecAlta().getYear() == year){
                Integer key = o.getClienteId().getId();
                double value;
                if(!distribuidorAcum.containsKey(key)){
                    value = 0;
                    distribuidorAcum.put(key, value);
                }
                else{
                    value = distribuidorAcum.get(key);
                }
                value += o.getTotalPago();
                distribuidorAcum.put(key, value);
            }
        }
        HashMap<Integer, Double> s = new HashMap<>();
        HashMap<Integer, Double> distribuidorTotalPagado = this.getDistributorSales(year);
        for(Map.Entry<Integer, Double> pair : distribuidorAcum.entrySet()){
            Double total1 = pair.getValue();
            Double total2 = distribuidorTotalPagado.get(pair.getKey());
            s.put(pair.getKey(), total1 - total2);
        }
     return s;
    }
}


