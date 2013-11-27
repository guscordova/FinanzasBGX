/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import entities.Componente;
import entities.Compra;
import entities.CompraComponente;
import entities.Proveedor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**

 @author user
 */
public class CompraDB {
    private int year;
    private NumericTableSet compras;
    private NumericTableSet comprasPorPieza;
    private NumericTableSet comprasPorProveedor;
    
    //  El constructor recibe un año y solo recaba registros del mismo
    public CompraDB(int year){
        this.compras = new NumericTableSet();
        this.comprasPorPieza = new NumericTableSet();
        this.comprasPorProveedor = new NumericTableSet();
        this.year = year;
    }
    
    /*
        Calcula total comprado (anual)
    */
    public double getTotalPurchaseYear(){
        //  Primero hacemos reducimos a los totales de cada mes
        //  despues hacemos una suma global
        return this.compras.getSumRow().getSum();
    }
    
    /*
        Calcula total comprado por mes
    */
    public List<Column> getTotalPurchaseMonth(){
        return this.compras.getSumRow().getDescendingColumns();
    }
    
    /*
        Obten total comprado por pieza
    */
    public List<Record> getTotalPurchaseComponent(){
        List<Record> records = new ArrayList<>();
        for(Column c : this.comprasPorPieza.getSumRow().getDescendingColumns()){
            records.add(this.getRecord(c));
        }
        return records;
    }
    
    /*
        Obten total comprado por proveedor
    */
    public List<Column> getTotalPurchaseSupplier(){
        return this.comprasPorProveedor.getSumRow().getAscendingColumns();
    }
    
    /*
        Funciones auxiliares
    */
    
    public Record getRecord(Column c){
        String[] ls = c.name.split(" ");
        int lsn = ls[0].length();
        Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
        return rcd;
    }
    
    /*
        Agregar compra
    */
    public void add(Compra compra){
        Calendar cal = Calendar.getInstance();
        cal.setTime(compra.getFecPago());
        if (cal.get(Calendar.YEAR) == this.year) {
            String strMonth = DateUtils.getMonth(Calendar.MONTH);
            double total = compra.getCostoTotal();
            this.compras.addValue(strMonth, total);
            //  La pieza solo tiene un proveedor
            Proveedor proveedor = compra.getProveedorId();
            String proveedorName = proveedor.getRfc();
            String p_key = proveedorName;
            this.comprasPorProveedor.addValue(p_key, total);
            //  Recorremos todos los componentes de esta compra
            //  Compra tiene una relacion de uno a muchos con CompraComponente
            for(CompraComponente compra_componente : compra.getCompraComponenteCollection()){
                Componente componente = compra_componente.getComponente();
                String pieza = componente.getNombre();
                String c_id = componente.getId().toString();
                String c_key = c_id + " " + pieza;
                //  Extraemos el precio de la pieza dentro de esta compra
                //  La sumatoria de los precios de todas las piezas debe dar
                //  el total de la compra
                double pieza_total = compra_componente.getCosto();
                this.comprasPorPieza.addValue(c_key, pieza_total);
            }
        }
    }
    
}
