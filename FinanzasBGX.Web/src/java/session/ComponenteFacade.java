/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Componente;
import entities.Proveedor;
import entities.ProveedorComponente;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;

/**
 *
 * @author robertogarza
 */
@Stateless
public class ComponenteFacade extends AbstractFacade<Componente> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComponenteFacade() {
        super(Componente.class);
    }
    
    
    public List<Componente> getComponents(int idSupplier)
    {
        List<Componente> components = new ArrayList<Componente>();
        if (idSupplier == -1)
            components = findAll();
        else {
            for (Componente c : findAll()) {
                for (ProveedorComponente pc : c.getProveedorComponenteCollection()) {
                    if (pc.getProveedor().getId() == idSupplier) 
                        components.add(c);
                }
            }
        }
        return components;
    }
}
