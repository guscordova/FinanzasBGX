package jsf;

import entities.ModeloComponente;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.ModeloComponenteFacade;

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

@Named("modeloComponenteController")
@SessionScoped
public class ModeloComponenteController implements Serializable {

    private ModeloComponente current;
    private DataModel items = null;
    @EJB
    private session.ModeloComponenteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ModeloComponenteController() {
    }

    public ModeloComponente getSelected() {
        if (current == null) {
            current = new ModeloComponente();
            current.setModeloComponentePK(new entities.ModeloComponentePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ModeloComponenteFacade getFacade() {
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
        current = (ModeloComponente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ModeloComponente();
        current.setModeloComponentePK(new entities.ModeloComponentePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getModeloComponentePK().setComponenteId(current.getComponente().getId());
            current.getModeloComponentePK().setModeloId(current.getModelo().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ModeloComponenteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ModeloComponente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getModeloComponentePK().setComponenteId(current.getComponente().getId());
            current.getModeloComponentePK().setModeloId(current.getModelo().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ModeloComponenteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ModeloComponente) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ModeloComponenteDeleted"));
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

    public ModeloComponente getModeloComponente(entities.ModeloComponentePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = ModeloComponente.class)
    public static class ModeloComponenteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModeloComponenteController controller = (ModeloComponenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modeloComponenteController");
            return controller.getModeloComponente(getKey(value));
        }

        entities.ModeloComponentePK getKey(String value) {
            entities.ModeloComponentePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.ModeloComponentePK();
            key.setModeloId(Integer.parseInt(values[0]));
            key.setComponenteId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(entities.ModeloComponentePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getModeloId());
            sb.append(SEPARATOR);
            sb.append(value.getComponenteId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ModeloComponente) {
                ModeloComponente o = (ModeloComponente) object;
                return getStringKey(o.getModeloComponentePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ModeloComponente.class.getName());
            }
        }
    }
}
