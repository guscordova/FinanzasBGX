/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.CompraComponente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author robertogarza
 */
@Stateless
public class CompraComponenteFacade extends AbstractFacade<CompraComponente> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompraComponenteFacade() {
        super(CompraComponente.class);
    }
    
}
