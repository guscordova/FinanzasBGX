package jsf;

import entities.Venta;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.VentaFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import utilidades.Column;
import utilidades.Record;

@Named("ventaController")
@SessionScoped
public class VentaController implements Serializable {

    private String currentYear;
    private String currentDistributor;
    private String currentDistributor2;
    private double totalSalesYear = 0.0;
    private DataModel totalSalesMonth = null;
    private DataModel totalSalesDistributorYear = null;
    private DataModel totalSalesDistributorCompare = null;
    private DataModel totalSalesDistributorCompare2 = null;
    
    private Venta current;
    private DataModel items = null;
    @EJB
    private session.VentaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private double pendienteCobrarAnual = 0.0;
    private DataModel pendienteCobrarDistribuidor = null;
    
    public VentaController() {
        //  Fijamos el año actual por defecto, si este no ha sido cambiado
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        this.currentDistributor = "-1";
    }
    
    @PostConstruct
    public void init() {
        this.totalSalesMonth = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(currentYear), -1));
        this.totalSalesDistributorYear = new ListDataModel(this.getFacade().getDistributorSalesTable(Integer.parseInt(currentYear), -1));
    }
    
    public String getCurrentYear(){
        return currentYear;
    }
    
    public void setCurrentYear(String year){
        this.currentYear = year;
    }
    
    public String getCurrentDistributor() {
        return currentDistributor;
    }
    
    public void setCurrentDistributor(String currentDistributor) {
        this.currentDistributor = currentDistributor;
    }
    
    public String getCurrentDistributor2() {
        return currentDistributor2;
    }
    
    public void setCurrentDistributor2(String currentDistributor) {
        this.currentDistributor2 = currentDistributor;
    }
    
    /*
        Ventas
    */
    
    /*
        Generales
    */
    public double getTotalSalesYear(){
        this.totalSalesYear  = this.getFacade().getTotalSalesYear(Integer.parseInt(currentYear));
        return this.totalSalesYear;
    }

    public DataModel getTotalSalesMonth(){
        return totalSalesMonth;
    }
    
    public DataModel getTotalSalesDistributorYear(){
        return totalSalesDistributorYear;
    }
    
    public DataModel getTotalSalesDistributorCompare(){
        return totalSalesDistributorCompare;
    }
    
    public DataModel getTotalSalesDistributorCompare2(){
        return totalSalesDistributorCompare2;
    }
    
    public String searchMonth()
    {
        this.totalSalesMonth = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(currentYear), Integer.parseInt(currentDistributor)));
        return "";
    }
    
    public String searchDistributor()
    {
        this.totalSalesDistributorYear = new ListDataModel(this.getFacade().getDistributorSalesTable(Integer.parseInt(currentYear), Integer.parseInt(currentDistributor)));
        return "";
    }

    public String compareDistributor()
    {
        this.totalSalesDistributorCompare = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(currentYear), Integer.parseInt(currentDistributor)));
        this.totalSalesDistributorCompare2 = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(currentYear), Integer.parseInt(currentDistributor2)));
        return "";
    }
    
    
    
    
    
    /*
        Pendiente por cobrar
    */
    public double getPendienteCobrarAnual(){
        this.pendienteCobrarAnual = this.getFacade().getPendienteCobrarAnual(Integer.parseInt(currentYear));
        return this.pendienteCobrarAnual;
    }
    
    public List<Record> getPendienteCobrarDistribuidor(){
        List<Record> l = this.getFacade().getPendienteCobrarDistribuidor(Integer.parseInt(currentYear));
        this.pendienteCobrarDistribuidor = new ListDataModel(l);
        return l;
    }
    
    //
    
    public Venta getSelected() {
        if (current == null) {
            current = new Venta();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VentaFacade getFacade() {
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
        current = (Venta) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Venta();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VentaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Venta) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VentaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Venta) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VentaDeleted"));
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

    public Venta getVenta(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Venta.class)
    public static class VentaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VentaController controller = (VentaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ventaController");
            return controller.getVenta(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Venta) {
                Venta o = (Venta) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Venta.class.getName());
            }
        }
    }
}
