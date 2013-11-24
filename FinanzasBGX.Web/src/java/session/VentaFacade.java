/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Orden;
import entities.Venta;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Root;
import utilidades.Column;
import utilidades.DateUtils;
import utilidades.NumericTableSet;
import utilidades.NumericTreeRow;

/**
 *
 * @author robertogarza
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
        Recaba las ordenes junto con su respectivo status
    */
    public List<Orden> OS(){
        Query q = em.createNamedQuery("Orden.findPendientesProducir");
        return q.getResultList();
    }

    
    /*
        Ventas
    */
    
    public double getYearSales(int year) {
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
    
    public NumericTableSet getMonthSalesTable(int year, String month){
        NumericTableSet salesSum = new NumericTableSet();
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                if(strMonth.equals(month) || month.equals("*")){
                    double total = v.getMonto() * v.getCantidad();
                    salesSum.addValue(strMonth, total);
                }
            }
        }
        return salesSum;
    }

    public List<Column> getMonthSales(int year, String month) {
        return this.getMonthSalesTable(year, month).getSumRow().getDescendingColumns();
    }
    
    public NumericTableSet getDistributorSalesTable(int year) {
        NumericTableSet salesAcum = new NumericTableSet();
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                String distribuidor = v.getOrdenId().getClienteId().getId().toString();
                double total = v.getCantidad() * v.getMonto();
                salesAcum.addValue(distribuidor, total);
            }
        }
        return salesAcum;
    }
    
    public List<Column> getDistributorSales(int year) {
        return this.getDistributorSalesTable(year).getSumRow().getDescendingColumns();
    }
    
    /*
        Pendiente por cobrar
    */
    
    public double getPendienteCobrarAnual(int year) {
        double orderSum = 0;
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                orderSum += o.getTotalPago();
            }
        }
        //  El monto total de las ordenes menos el monto que ha sido pagado
        return orderSum - this.getYearSales(year);
    }

    public List<Column> getPendienteCobrarMensual(int year, String month) {
        NumericTableSet orderAcum = new NumericTableSet();
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                if(strMonth.equals(month) || month.equals("*")){
                    double total = o.getTotalPago();
                    orderAcum.addValue(strMonth, total);
                }
            }
        }
        //  Obtengo las ventas mensuales por mes
        NumericTableSet salesAcum = this.getMonthSalesTable(year, month);
        NumericTreeRow difference = orderAcum.getSumRow();
        //  Ahora, al monto total por mes de las ordenes, le resto las ventas totales
        //  de cada mes
        difference.substract(salesAcum.getSumRow());
        return difference.getDescendingColumns();
    }

    public NumericTreeRow getPendienteCobrarDistribuidor(int year) {
        NumericTableSet acumOrder = new NumericTableSet();
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                String distribuidor = o.getClienteId().getId().toString();
                double total = o.getTotalPago();
                acumOrder.addValue(distribuidor, total);
            }
        }
        //  Realiza la sumatoria de los totales de las ordenesde cada uno de los distribuidores
        NumericTreeRow orderSum = acumOrder.getSumRow();
        //  A estos totales, restar los totales de las ventas realizadas por distribuidor
        orderSum.substract(this.getDistributorSalesTable(year).getSumRow());
        return orderSum;
    }
    
    /*
        Pendiente por producir
    */
    

}
