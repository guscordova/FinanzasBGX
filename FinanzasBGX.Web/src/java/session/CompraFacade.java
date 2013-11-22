/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Compra;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utilidades.CompraCore;
import utilidades.NumericTreeRow;

/**
 *
 * @author robertogarza
 */
@Stateless
public class CompraFacade extends AbstractFacade<Compra> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;
    //
    private CompraCore cu;

    public CompraFacade() {
        super(Compra.class);
        this.cu = new CompraCore();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public double getTotalPurchasesYear(int year){
        return this.cu.getYearPurchases(year);
    }
    
    public double[] getTotalPurchasesMonth(int year){
        return this.cu.getMonthPurchases(year);
    }
    
    public NumericTreeRow getSupplierPurchases(int year){
        return this.cu.getSupplierPurchases(year);
    }
    
}
