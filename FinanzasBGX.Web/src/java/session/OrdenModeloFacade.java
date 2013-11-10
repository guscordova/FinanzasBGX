/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.OrdenModelo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author robertogarza
 */
@Stateless
public class OrdenModeloFacade extends AbstractFacade<OrdenModelo> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdenModeloFacade() {
        super(OrdenModelo.class);
    }
    
}
