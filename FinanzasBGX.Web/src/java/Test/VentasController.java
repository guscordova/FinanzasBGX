/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import entities.Venta;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import session.VentaFacade;

/**
 *
 * @author user
 */
@ManagedBean(name = "VentasController", eager = false)
@SessionScoped
public class VentasController extends BaseController<VentaFacade, Venta> implements Serializable {
    
    public VentasController(){
    }
    
    @Override
    protected Venta createNew() {
        return new Venta();
    }

    @Override
    protected int getControllerItemsCount() {
        return this.getFacade().count();
    }

    @Override
    protected DataModel createControllerPageDataModel(int firstItem, int lastItem) {
        return new ListDataModel(this.getFacade().findRange(new int[]{firstItem, lastItem}));
    }
    
}
