/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import entities.Cliente;
import entities.Orden;
import entities.Venta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**

 @author user
 */
public class VentaDB {
    private int year;
    private NumericTableSet ventas;
    private NumericTableSet ventasPorDistribuidor;
    private NumericTableSet ventasPorDistribuidorID;
    private NumericTableSet pendientePorCobrar;
    private NumericTableSet pendientePorCobrarPorDistribuidor;
    private NumericTableSet pendientePorProducir;
    
    public VentaDB(int year){
        this.year = year;
        this.ventas = new NumericTableSet();
        this.ventasPorDistribuidor = new NumericTableSet();
        this.pendientePorCobrar = new NumericTableSet();
        this.pendientePorProducir = new NumericTableSet();
        this.ventasPorDistribuidorID = new NumericTableSet();
    }
    
    /*
        Calcula total vendido (anual)
    */
    public double getTotalSalesYear(){
        return this.ventas.getSumRow().getSum();
    }
    
    /*
        Calcula total vendido (mes)
    */
    public List<Column> getTotalSalesMonth(){
        return this.ventas.getSumRow().getDescendingColumns();
    }
    
    /*
        Calcula total vendido a distribuidores
    */
    public List<Record> getDistributorSales(){
        List<Record> records = new ArrayList<>();
        for(Column c : this.ventasPorDistribuidor.getSumRow().getDescendingColumns()){
            records.add(this.getRecord(c));
        }
        return records;
    }
    
    /*
        Comparar distribuidores
    */
    public double compararDistribuidores(String dist1, String dist2){
        double dsum1 = this.ventasPorDistribuidorID.getSumColumn(dist1);
        double dsum2 = this.ventasPorDistribuidorID.getSumColumn(dist2);
        return dsum1 - dsum2;
    }
    
    /*
        Calcula pendiente por cobrar
    */
    public double getPendienteCobrarAnual(){
        return this.pendientePorCobrar.getSumRow().getSum();
    }
    
    /*
        Calcula pendiente por cobrar por distribuidor
    */
    public List<Record> getPendienteCobrarDistribuidor(){
        List<Record> records = new ArrayList<>();
        for(Column c : this.pendientePorCobrarPorDistribuidor.getSumRow().getDescendingColumns()){
            records.add(this.getRecord(c));
        }
        return records;
    }
    
    /*
        Calcula pendiente por producir
    */
    public double getPendienteProducirYear(){
        return this.pendientePorProducir.getSumRow().getSum();
    }
    
    public Record getRecord(Column c){
        String[] ls = c.name.split(" ");
        int lsn = ls[0].length();
        Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
        return rcd;
    }
    
    
    public void add(Orden orden) {
        //  Recorremos las ventas (pagos) hechas para la orden actual
        double ventaSum = 0;
        for(Venta venta : orden.getVentaCollection()){
            Calendar ventaDate = Calendar.getInstance();
            ventaDate.setTime(venta.getFecCobro());
            if(ventaDate.get(Calendar.YEAR) == this.year){
                String ventaMonth = DateUtils.getMonth(ventaDate.get(Calendar.MONTH));
                double ventaTotal = venta.getMonto() * venta.getCantidad();
                this.ventas.addValue(ventaMonth, ventaTotal);
                //  Por distribuidor
                Cliente cliente = venta.getOrdenId().getClienteId();
                String distribuidorID = cliente.getId().toString();
                String distributorName = cliente.getNombre() + " " + cliente.getAppaterno() + " " + cliente.getApmaterno();
                //  Anexamos el id solamente para fines de que sea unica la entrada
                String key = distribuidorID + " " + distributorName;
                this.ventasPorDistribuidor.addValue(key, ventaTotal);
                this.ventasPorDistribuidorID.addValue(distribuidorID, ventaSum);
                ventaSum += ventaTotal;
            }           
        }
        //  Ahora calculamos los pendientes por cobrar!!!
        Calendar ordenDate = Calendar.getInstance();
        ordenDate.setTime(orden.getFecAlta());
        if(ordenDate.get(Calendar.YEAR) == this.year){
            String ordenMonth = DateUtils.getMonth(ordenDate.get(Calendar.MONTH));
            double ordenTotal = orden.getTotalPago();
            double pc = ordenTotal - ventaSum;
            this.pendientePorCobrar.addValue(ordenMonth, pc);
            //  Por distribuidor
            Cliente cliente = orden.getClienteId();
            String distribuidorID = cliente.getId().toString();
            String distributorName = cliente.getNombre() + " " + cliente.getAppaterno() + " " + cliente.getApmaterno();
            //  Anexamos el id solamente para fines de que sea unica la entrada
            String key = distribuidorID + " " + distributorName;
            this.pendientePorCobrarPorDistribuidor.addValue(key, pc);
            if(orden.getEstatusId().getId() < 5){
                this.pendientePorProducir.addValue(ordenMonth, ordenTotal);
            }
        }
    }
}
