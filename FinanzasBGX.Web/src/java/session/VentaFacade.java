/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Venta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utilidades.NumericTreeRow;
import utilidades.VentaCore;

/**
 *
 * @author robertogarza
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;
    //
    private VentaCore vu;
    
    public VentaFacade() {
        super(Venta.class);
        this.vu = new VentaCore();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public double getTotalSalesYear(int year){
        return this.vu.getYearSales(year);
    }
    
    public double[] getTotalSalesMonth(int year){
        return this.vu.getMonthSales(year);
    }
    
    public NumericTreeRow getDistributorSales(int year){
        return this.vu.getDistributorSales(year);
    }
    
    public double getPendienteCobrarAnual(int year){
        return this.vu.getPendienteCobrarAnual(year);
    }
    
    public double[] getPendienteCobrarMes(int year){
        return this.vu.getPendienteCobrarMensual(year);
    }
    
    public NumericTreeRow getPenienteCobrarDistribuidor(int year){
        return this.vu.getPendienteCobrarDistribuidor(year);
    }

}
