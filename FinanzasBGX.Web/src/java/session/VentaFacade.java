/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utilidades.ventaUtils;

/**
 *
 * @author robertogarza
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public double getTotalSalesYear(int year){
        List<Venta> ventas = this.findAll();
        ventaUtils vu = new ventaUtils(ventas);
        return vu.getYearSales(year);
    }
    
    public double[] getTotalSalesMonth(int year){
        List<Venta> ventas = this.findAll();
        ventaUtils vu = new ventaUtils(ventas);
        return vu.getMonthSales(year);
    }
    
    

    public VentaFacade() {
        super(Venta.class);
    }
    
}
