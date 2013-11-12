/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Compra;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utilidades.compraUtils;

/**
 *
 * @author robertogarza
 */
@Stateless
public class CompraFacade extends AbstractFacade<Compra> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompraFacade() {
        super(Compra.class);
    }
    
    public double getTotalPurchasesYear(int year){
        List<Compra> compras = this.findAll();
        compraUtils cu = new compraUtils(compras);
        return cu.getYearPurchases(year);
    }
    
    public double[] getTotalPurchasesMonth(int year){
        List<Compra> compras = this.findAll();
        compraUtils cu = new compraUtils(compras);
        return cu.getMonthPurchases(year);
    }
    
}
