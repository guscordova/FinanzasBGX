package jsf;

import entities.PedidoComponente;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.PedidoComponenteFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("pedidoComponenteController")
@SessionScoped
public class PedidoComponenteController implements Serializable {

    private PedidoComponente current;
    private DataModel items = null;
    @EJB
    private session.PedidoComponenteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PedidoComponenteController() {
    }

    public PedidoComponente getSelected() {
        if (current == null) {
            current = new PedidoComponente();
            current.setPedidoComponentePK(new entities.PedidoComponentePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private PedidoComponenteFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (PedidoComponente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new PedidoComponente();
        current.setPedidoComponentePK(new entities.PedidoComponentePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getPedidoComponentePK().setPedidoId(current.getPedido().getId());
            current.getPedidoComponentePK().setComponenteId(current.getComponente().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoComponenteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (PedidoComponente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getPedidoComponentePK().setPedidoId(current.getPedido().getId());
            current.getPedidoComponentePK().setComponenteId(current.getComponente().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoComponenteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (PedidoComponente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoComponenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public PedidoComponente getPedidoComponente(entities.PedidoComponentePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PedidoComponente.class)
    public static class PedidoComponenteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidoComponenteController controller = (PedidoComponenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidoComponenteController");
            return controller.getPedidoComponente(getKey(value));
        }

        entities.PedidoComponentePK getKey(String value) {
            entities.PedidoComponentePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.PedidoComponentePK();
            key.setPedidoId(Integer.parseInt(values[0]));
            key.setComponenteId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(entities.PedidoComponentePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPedidoId());
            sb.append(SEPARATOR);
            sb.append(value.getComponenteId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PedidoComponente) {
                PedidoComponente o = (PedidoComponente) object;
                return getStringKey(o.getPedidoComponentePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PedidoComponente.class.getName());
            }
        }
    }
}
