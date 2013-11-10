/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.ProveedorComponente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author robertogarza
 */
@Stateless
public class ProveedorComponenteFacade extends AbstractFacade<ProveedorComponente> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorComponenteFacade() {
        super(ProveedorComponente.class);
    }
    
}
