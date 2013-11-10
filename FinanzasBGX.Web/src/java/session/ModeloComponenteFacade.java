/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.ModeloComponente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author robertogarza
 */
@Stateless
public class ModeloComponenteFacade extends AbstractFacade<ModeloComponente> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModeloComponenteFacade() {
        super(ModeloComponente.class);
    }
    
}
