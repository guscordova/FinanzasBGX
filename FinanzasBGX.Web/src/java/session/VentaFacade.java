/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Cliente;
import entities.Estatus;
import entities.Orden;
import entities.Venta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import utilidades.Column;
import utilidades.DateUtils;
import utilidades.NumericTableSet;
import utilidades.NumericTreeRow;
import utilidades.Record;

/*
    Implementado:
        Calcula total vendido por año
        Calcula total vendido por mes
        Calcula total vendido por distribuidor
        Comparar distribuidores
        Calcula pendiente por cobrar
        Calcula pendiente por cobrar por distribuidor
        Calcula pendiente por producir
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;
    //
    
    public VentaFacade() {
        super(Venta.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /*
        Recaba todas las ventas de un año dado
    */
    public List<Venta> V(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Venta> venta = cq.from(Venta.class);
        cq.select(venta);
        return this.em.createQuery(cq).getResultList();
    }
    
    /*
        Recaba todas las ordenes realizadas en un año dado
    */
    public List<Orden> O(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Orden> orden = cq.from(Orden.class);
        cq.select(orden);
        return this.em.createQuery(cq).getResultList();
    }
    
    /*
        Recaba un cliente dado su id (distribuidor)
    */
    public Cliente CById(Integer clientID){
        return this.em.find(Cliente.class, clientID);
    }
    
    /*
        Venta Controller functions
    */
    
    /*
        Calcula total vendido por año
        Obtiene unicamente las ventas del año especificado y obtiene su sumatoria
    */
    public double getTotalSalesYear(int year) {
        double salesSum = 0;
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                salesSum += v.getMonto() * v.getCantidad();
            }
        }
        return salesSum;
    }
    
    /*
        Calcula total vendido por mes (auxiliar)
    */
    private NumericTableSet getMonthSalesTable(int year){
        NumericTableSet salesSum = DateUtils.getDatedTableSet();
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                double total = v.getMonto() * v.getCantidad();
                salesSum.addValue(strMonth, total);
            }
        }
        return salesSum;
    }
    
    /*
        Calcula total vendido por mes
        Obtiene unicamente las ventas del año especificado y organiza las 
        sumatoria por meses
    */
    public List<Column> getMonthSales(int year) {
        return this.getMonthSalesTable(year).getSumRow().getDescendingColumns();
    }
    
    /*
        Calcula total vendido a distribuidores (auxiliar)
    */
    private NumericTableSet getDistributorSalesTable(int year) {
        NumericTableSet salesAcum = new NumericTableSet();
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                Cliente cliente = v.getOrdenId().getClienteId();
                String distribuidorID = cliente.getId().toString();
                String distributorName = cliente.getNombre() + " " + cliente.getAppaterno();
                //  Anexamos el id solamente para fines de que sea unica la entrada
                String key = cliente.getId().toString() + " " + distribuidorID + " " + distributorName;
                double total = v.getCantidad() * v.getMonto();
                salesAcum.addValue(key, total);
            }
        }
        return salesAcum;
    }
    
    /*
        Calcula total vendido a distribuidores
        Obtiene unicamente las ventas del año especificado y las organiza
        por distribuidores (clientes)
    */
    public List<Record> getDistributorSales(int year) {
        NumericTreeRow table = this.getDistributorSalesTable(year).getSumRow();
        List<Record> records = new ArrayList<>();
        for(Column c : table.getDescendingColumns()){
            //  Separamos el id del nombre del cliente
            String[] ls = c.name.split(" ");
            int lsn = ls[0].length();
            Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
            records.add(rcd);
        }
        return records;
    }
    
    /*
       Comparar distribuidores (auxiliar)
    */
    public double getDistributorSalesById(int year, Integer distributorID){
        Cliente c = this.CById(distributorID);
        double sum = 0;
        //  Para cada distribuidor (cliente) recorremos sus ordenes, y por cada
        //  orden recorremos las ventas filtradas por año
        for(Orden o : c.getOrdenCollection()){
            for(Venta v : o.getVentaCollection()){
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) == year) {
                    double total = v.getCantidad() * v.getMonto();
                    sum += total;
                }
            }
        }
        return sum;
    }
    
    /*
        Comparar distribuidores
    */
    public double compareDistributorSales(int year, Integer dist1, Integer dist2){
        //  Calculamos las ventas totales de cada uno y obtenemos su diferencia
        double c1sum = getDistributorSalesById(year, dist1);
        double c2sum = getDistributorSalesById(year, dist2);
        return c1sum - c2sum;
    }

    /*
        Calcula pendiente por cobrar total
    */
    public double getPendienteCobrarAnual(int year) {
        double orderSum = 0;
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                orderSum += o.getTotalPago();
                //  Ahora vemos los pagos que han sido realizados a esta orden
                //  y los descontamos al total
                for(Venta v : o.getVentaCollection()){
                    orderSum -= v.getCantidad() * v.getMonto();
                }
            }
        }
        return orderSum;
    }
    
    /*
        Calcula pendiente por cobrar total por distribuidor (auxiliar)
    */
    public NumericTableSet getPendienteCobrarDistribuidorTable(int year) {
        NumericTableSet acumOrder = new NumericTableSet();
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                Cliente cliente = o.getClienteId();
                String distribuidorID = cliente.getId().toString();
                String distributorName = cliente.getId() + " " + cliente.getNombre() + " " + cliente.getAppaterno();
                String key = distribuidorID + " " + distributorName;
                double total = o.getTotalPago();
                //  Ahora a esta orden, le vamos a descontar todos los pagos realizados
                for(Venta v : o.getVentaCollection()){
                    total -= v.getCantidad() * v.getMonto();
                }
                acumOrder.addValue(key, total);
            }
        }
        //  Realiza la sumatoria de los totales de las ordenesde cada uno de los distribuidores
        return acumOrder;
    }
    
    /*
        Calcula pendiente por cobrar total por distribuidor
    */
   public List<Record> getPendienteCobrarDistribuidor(int year) {
        NumericTreeRow table = this.getPendienteCobrarDistribuidorTable(year).getSumRow();
        List<Record> records = new ArrayList<>();
        for(Column c : table.getDescendingColumns()){
            //  Separamos el id del nombre del cliente
            String[] ls = c.name.split(" ");
            int lsn = ls[0].length();
            Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
            records.add(rcd);
        }
        return records;
    }
    
    /*
        Pendiente por producir total
    */
    public double getPendienteProducirYear(int year) {
        double orderSum = 0;
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                Estatus estatus = o.getEstatusId();
                //  5 = producido
                if(estatus.getId() < 5){
                    orderSum = orderSum + o.getTotalPago();
                }
            }
        }
        return orderSum;
    }
}
