/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import entities.Venta;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.VentaFacade;

/**
 *
 * @author user
 */
public abstract class BaseController<F, E> {
    protected  E current;
    @EJB
    protected  F ejbFacade;
    protected  DataModel items = null;
    protected  int selectedItemIndex;
    protected  PaginationHelper pagination;
    
    protected abstract E createNew();
    protected abstract int getControllerItemsCount();
    protected abstract DataModel createControllerPageDataModel(int firstItem, int lastItem);

    public BaseController() {
    }
    
    protected F getFacade() {
        return ejbFacade;
    }
    
    protected E getSelected() {
        if (current == null) {
            current = this.createNew();
            selectedItemIndex = -1;
        }
        return current;
    }

    protected PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getControllerItemsCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    int firstItem = this.getPageFirstItem();
                    int lastItem = firstItem + this.getPageSize();
                    return createControllerPageDataModel(firstItem, lastItem);
                }
            };
        }
        return pagination;
    }
}
