package jsf;

import dto.CompraMes;
import entities.Compra;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.CompraFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import utilidades.CompraDB;

import nz.co.kevindoran.googlechartsjsf.DefaultGoogleChartModel;
import nz.co.kevindoran.googlechartsjsf.GoogleChartModel;
import nz.co.kevindoran.googlechartsjsf.Row;
import nz.co.kevindoran.googlechartsjsf.Column;

@Named("compraController")
@SessionScoped
public class CompraController implements Serializable {

    private List<String> years;
    
    private String monthViewCurrentYear;
    private String monthViewCurrentComponent;
    private String monthViewCurrentSupplier;
    private String monthViewSearch;
    
    private Date supplierViewStartDate;
    private Date supplierViewEndDate;
    private String supplierViewCurrentComponent;
    private String supplierViewCurrentSupplier;
    private String supplierViewSearch;
    
    private Date componentViewStartDate;
    private Date componentViewEndDate;
    private String componentViewCurrentComponent;
    private String componentViewCurrentSupplier;
    private String componentViewSearch;
    
    private double totalPurchasesYear = 0.0;
    private double totalPurchasesActualMonth = 0.0;
    
    private List<CompraMes> totalPurchasesMonth = null;
    private DataModel totalPurchasesComponent = null;
    private DataModel totalPurchasesSupplier = null;
    
    private String graphViewCurrentYear;
    private String graphYear;
    private GoogleChartModel chartModelCurrentYear = new DefaultGoogleChartModel("LineChart");
    private GoogleChartModel chartModelPastYears = new DefaultGoogleChartModel("LineChart");
    
    @EJB
    private session.CompraFacade ejbFacade;
    
    
    
    
    
    
    private Compra current;
    private DataModel items = null;
    private CompraDB compraDB = null;
   
    private PaginationHelper pagination;
    private int selectedItemIndex;
    

    public CompraController() {
        this.monthViewCurrentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 01);
        this.supplierViewStartDate = cal1.getTime();
        this.componentViewStartDate = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
        this.supplierViewEndDate =  cal2.getTime();
        this.componentViewEndDate = cal2.getTime();
        this.monthViewCurrentComponent = "-1";
        this.monthViewCurrentSupplier = "-1";
        this.componentViewCurrentComponent = "-1";
        this.componentViewCurrentSupplier = "-1";
        this.supplierViewCurrentComponent = "-1";
        this.supplierViewCurrentSupplier = "-1";
        this.graphViewCurrentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
    }
    
    @PostConstruct
    public void init() {
        this.totalPurchasesMonth = (this.getFacade().getMonthPurchases(
                                                          Integer.parseInt(monthViewCurrentYear), 
                                                          Integer.parseInt(monthViewCurrentSupplier), 
                                                          Integer.parseInt(monthViewCurrentComponent)));
        this.totalPurchasesSupplier = new ListDataModel(this.getFacade().getSupplierPurchases(
                                                          supplierViewStartDate, supplierViewEndDate,
                                                          Integer.parseInt(supplierViewCurrentSupplier), 
                                                          Integer.parseInt(supplierViewCurrentComponent)));
        this.totalPurchasesComponent = new ListDataModel(this.getFacade().getComponentPurchases(
                                                          componentViewStartDate, componentViewEndDate,
                                                          Integer.parseInt(componentViewCurrentSupplier), 
                                                          Integer.parseInt(componentViewCurrentComponent)));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear + ",   proveedor Todos, componente Todos";
        this.supplierViewSearch = "Busqueda entre " +  df.format(supplierViewStartDate) + " y " + df.format(supplierViewEndDate) + ",   proveedor Todos, componente Todos";
        this.componentViewSearch = "Busqueda entre " +  df.format(componentViewStartDate) + " y " + df.format(componentViewEndDate) + ",   proveedor Todos, componente Todos";
    
        loadGraphMonths(graphViewCurrentYear);
        loadGraphYears();
    }

    public String getMonthViewCurrentYear() {
        return monthViewCurrentYear;
    }

    public String getMonthViewCurrentComponent() {
        return monthViewCurrentComponent;
    }

    public String getMonthViewCurrentSupplier() {
        return monthViewCurrentSupplier;
    }
    
    public String getMonthViewSearch() {
        return monthViewSearch;
    }
    
    public Date getComponentViewStartDate() {
        return componentViewStartDate;
    }
    
    public Date getComponentViewEndDate() {
        return componentViewEndDate;
    }
    
    public String getComponentViewCurrentComponent() {
        return componentViewCurrentComponent;
    }

    public String getComponentViewCurrentSupplier() {
        return componentViewCurrentSupplier;
    }
    
    public String getSupplierViewSearch() {
        return supplierViewSearch;
    }

    public Date getSupplierViewStartDate() {
        return supplierViewStartDate;
    }
    
    public Date getSupplierViewEndDate() {
        return supplierViewEndDate;
    }
    
    public String getSupplierViewCurrentComponent() {
        return supplierViewCurrentComponent;
    }

    public String getSupplierViewCurrentSupplier() {
        return supplierViewCurrentSupplier;
    }
    
    public String getComponentViewSearch() {
        return componentViewSearch;
    }

    public void setMonthViewCurrentYear(String monthViewCurrentYear) {
        this.monthViewCurrentYear = monthViewCurrentYear;
    }

    public void setMonthViewCurrentComponent(String monthViewCurrentComponent) {
        this.monthViewCurrentComponent = monthViewCurrentComponent;
    }

    public void setMonthViewCurrentSupplier(String monthViewCurrentSupplier) {
        this.monthViewCurrentSupplier = monthViewCurrentSupplier;
    }

    public void setComponentViewStartDate(Date componentViewStartDate) {
        this.componentViewStartDate = componentViewStartDate;
    }
    
    public void setComponentViewEndDate(Date componentViewEndDate) {
        this.componentViewEndDate = componentViewEndDate;
    }

    public void setComponentViewCurrentComponent(String componentViewCurrentComponent) {
        this.componentViewCurrentComponent = componentViewCurrentComponent;
    }

    public void setComponentViewCurrentSupplier(String componentViewCurrentSupplier) {
        this.componentViewCurrentSupplier = componentViewCurrentSupplier;
    }

    public void setSupplierViewStartDate(Date supplierViewStartDate) {
        this.supplierViewStartDate = supplierViewStartDate;
    }
    
    public void setSupplierViewEndDate(Date supplierViewEndDate) {
        this.supplierViewEndDate = supplierViewEndDate;
    }

    public void setSupplierViewCurrentComponent(String supplierViewCurrentComponent) {
        this.supplierViewCurrentComponent = supplierViewCurrentComponent;
    }

    public void setSupplierViewCurrentSupplier(String supplierViewCurrentSupplier) {
        this.supplierViewCurrentSupplier = supplierViewCurrentSupplier;
    }
    
    public List<CompraMes> getTotalPurchasesMonth(){
        return totalPurchasesMonth;
    }
    
    public DataModel getTotalPurchasesSupplier(){
        return totalPurchasesSupplier;
    }
    
    public DataModel getTotalPurchasesComponent(){
        return totalPurchasesComponent;
    }
    
    public List<String> getYears() {
        this.years = this.getFacade().getYears();
        return years;
    }
    
    /*
        Calcula total comprado (anual)
    */
    public double getTotalPurchasesYear(){
        this.totalPurchasesYear = this.getFacade().getTotalPurchasesYear(Calendar.getInstance().get(Calendar.YEAR));
        return this.totalPurchasesYear;
    }
    
    /*
        Calcula total comprado (mens actual)
    */
    public double getTotalPurchasesActualMonth(){
        this.totalPurchasesActualMonth = this.getFacade().getTotalPurchasesActualMonth(Calendar.getInstance().get(Calendar.MONTH));
        return this.totalPurchasesActualMonth;
    }
    
    public String searchMonth()
    {
        this.totalPurchasesMonth = (this.getFacade().getMonthPurchases(
                                                          Integer.parseInt(monthViewCurrentYear), 
                                                          Integer.parseInt(monthViewCurrentSupplier), 
                                                          Integer.parseInt(monthViewCurrentComponent)));
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear +
                               ", proveedor " + this.getFacade().getSupplierNameById(Integer.parseInt(monthViewCurrentSupplier)) + 
                               ", componente " + this.getFacade().getComponentNameById(Integer.parseInt(monthViewCurrentComponent));
        return "";
    }
    
    public String searchSupplier()
    {
        this.totalPurchasesSupplier = new ListDataModel(this.getFacade().getSupplierPurchases(
                                                          supplierViewStartDate, supplierViewEndDate,
                                                          Integer.parseInt(supplierViewCurrentSupplier), 
                                                          Integer.parseInt(supplierViewCurrentComponent)));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.supplierViewSearch = "Busqueda entre " +  df.format(supplierViewStartDate) + " y " + df.format(supplierViewEndDate) + 
                                    ", proveedor " + this.getFacade().getSupplierNameById(Integer.parseInt(supplierViewCurrentSupplier)) + 
                                    ", componente " + this.getFacade().getComponentNameById(Integer.parseInt(supplierViewCurrentComponent));
        return "";
    }
    
    public String searchComponent()
    {
        this.totalPurchasesComponent = new ListDataModel(this.getFacade().getComponentPurchases(
                                                          componentViewStartDate, componentViewEndDate,
                                                          Integer.parseInt(componentViewCurrentSupplier), 
                                                          Integer.parseInt(componentViewCurrentComponent)));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.componentViewSearch = "Busqueda entre " +  df.format(componentViewStartDate) + " y " + df.format(componentViewEndDate) + 
                                    ", proveedor " + this.getFacade().getSupplierNameById(Integer.parseInt(componentViewCurrentSupplier)) + 
                                    ", componente " + this.getFacade().getComponentNameById(Integer.parseInt(componentViewCurrentComponent));
        return "";
    }
    
    /*
     * Gráficas
     */
    
    public String getGraphViewCurrentYear() {
        return graphViewCurrentYear;
    }
    
    public void setGraphViewCurrentYear(String graphViewCurrentYear) {
        this.graphViewCurrentYear = graphViewCurrentYear;
    }
    
    public String getGraphYear() {
        return graphYear;
    }
    
    private void loadGraphMonths(String year) {
         List<CompraMes> totalPurchases  = this.getFacade().getMonthPurchases(Integer.parseInt(year), 
                                                    -1, 
                                                    -1);

        chartModelCurrentYear.addColumn(new Column(Column.JavaScriptType.string, "Mes"));
        chartModelCurrentYear.addColumn(new Column(Column.JavaScriptType.number, "Compras"));
        
        DecimalFormat df = new DecimalFormat("#,###,###,###.##");
        int noOfRows = 2;
        for(CompraMes n: totalPurchases) {
            Row row = new Row(noOfRows);
            row.addEntry("'" + n.getMes() + "'");
            
            row.addEntry("{ v: " + String.valueOf(n.getTotal()) + ", f: '$" + df.format(n.getTotal()) + "'}");
            chartModelCurrentYear.addRow(row);
            
        }
        
        chartModelCurrentYear.setOptions("title:'" + graphViewCurrentYear + "', displayAnnotations:false"); // Simply inserted as javascript.  
        
        
        this.graphYear = "Busqueda por año " + year;
    }
    
    private void loadGraphYears( ) {
         int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        chartModelPastYears.addColumn(new Column(Column.JavaScriptType.string, "Año"));
        chartModelPastYears.addColumn(new Column(Column.JavaScriptType.number, "Compras"));
        
        DecimalFormat df = new DecimalFormat("#,###,###,###.##");
        int noOfRows = 2;
        for(int i=-10; i < 1; i++) {
            int y = currentYear + i;
            Row row = new Row(noOfRows);
            row.addEntry("'" + y + "'");
            double amount = this.getFacade().getTotalPurchasesYear(y);
            row.addEntry("{ v: " + amount + ", f: '$" + df.format(amount) + "'}");
            chartModelPastYears.addRow(row);
            
        }
        
        chartModelPastYears.setOptions("title:'Compras Historicas', displayAnnotations:false"); // Simply inserted as javascript.  

    }
     
     public void updateGraph( ) {
         chartModelCurrentYear = new DefaultGoogleChartModel("LineChart");
         loadGraphMonths(graphViewCurrentYear);
     }
     
     public GoogleChartModel getChartModelCurrentYear() {
        return chartModelCurrentYear;
     }
     
     public GoogleChartModel getChartModelPastYears() {
        return chartModelPastYears;
     }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
        Calcula total comprado por componente
    *//*
    public List<Record> getTotalPurchaseComponent(){
        List<Record> l = this.compraDB.getTotalPurchaseComponent();
        this.totalPurchasesComponent = new ListDataModel(l);
        return l;
    }
    */
    /*
        Calcula total comprado por proveedor
    *//*
    public List<Column> getTotalPurchaseSupplier(){
        List<Column> l = this.compraDB.getTotalPurchaseSupplier();
        this.totalPurchasesSupplier = new ListDataModel(l);
        return l;
    }
    */
    /*
     Gráficas
     */
   
    

    public Compra getSelected() {
        if (current == null) {
            current = new Compra();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CompraFacade getFacade() {
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
        current = (Compra) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Compra();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CompraCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Compra) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CompraUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Compra) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CompraDeleted"));
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

    public Compra getCompra(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Compra.class)
    public static class CompraControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompraController controller = (CompraController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "compraController");
            return controller.getCompra(getKey(value));
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
            if (object instanceof Compra) {
                Compra o = (Compra) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Compra.class.getName());
            }
        }
    }
}
