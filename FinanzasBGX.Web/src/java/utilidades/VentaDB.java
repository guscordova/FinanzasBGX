/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import entities.Cliente;
import entities.Venta;
import java.util.Calendar;

/**

 @author user
 */
public class VentaDB {
    private int year;
    private NumericTableSet ventas;
    private NumericTableSet ventasPorDistribuidor;
    private NumericTableSet pendientePorCobrar;
    
    public VentaDB(int year){
        this.year = year;
        this.ventas = new NumericTableSet();
        this.ventasPorDistribuidor = new NumericTableSet();
        this.pendientePorCobrar = new NumericTableSet();
    }
    
     public Record getRecord(Column c){
        String[] ls = c.name.split(" ");
        int lsn = ls[0].length();
        Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
        return rcd;
    }
    
    
    public void add(Venta venta) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(venta.getFecCobro());
        if (cal.get(Calendar.YEAR) == year) {
            String strMonth = DateUtils.getMonth(Calendar.MONTH);
            double total = venta.getMonto() * venta.getCantidad();
            this.ventas.addValue(strMonth, total);
            Cliente cliente = venta.getOrdenId().getClienteId();
            String distribuidorID = cliente.getId().toString();
            String distributorName = cliente.getNombre() + " " + cliente.getAppaterno() + " " + cliente.getApmaterno();
            //  Anexamos el id solamente para fines de que sea unica la entrada
            String key = distribuidorID + " " + distributorName;
            this.ventasPorDistribuidor.addValue(key, total);
            //  Ahora calculamos los pendientes por cobrar!!!
            
        }
    }
}
