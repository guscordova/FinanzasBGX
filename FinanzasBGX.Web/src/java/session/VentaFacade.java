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

    public double[] getMonthSales(int year) {
        double[] salesSum = new double[12];
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                salesSum[cal.get(Calendar.MONTH)] += v.getMonto() * v.getCantidad();
            }
        }
        return salesSum;
    }
    
    public NumericTreeRow getDistributorSales(int year) {
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
        return salesAcum.getSumRow();
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

    public double[] getPendienteCobrarMensual(int year) {
        double[] orderAcum = new double[12];
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                orderAcum[cal.get(Calendar.MONTH)] += o.getTotalPago();
            }
        }
        //  Obtengo las ventas mensuales por mes
        double[] salesAcum = this.getMonthSales(year);
        //  Ahora, al monto total por mes de las ordenes, le resto las ventas totales
        //  de cada mes
        for(int i = 0; i < orderAcum.length; i++){
            orderAcum[i] = orderAcum[i] - salesAcum[i];
        }
        return orderAcum;
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
        orderSum.substract(this.getDistributorSales(year));
        return orderSum;
    }
    
    /*
        Pendiente por producir
    */
    

}
