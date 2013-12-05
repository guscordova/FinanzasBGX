package jsf;

import dto.VentaMes;
import entities.Venta;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.VentaFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import nz.co.kevindoran.googlechartsjsf.DefaultGoogleChartModel;
import nz.co.kevindoran.googlechartsjsf.GoogleChartModel;
import nz.co.kevindoran.googlechartsjsf.Column;
import nz.co.kevindoran.googlechartsjsf.Row;
import utilidades.Record;
import java.text.DecimalFormat;

@Named("ventaController")
@SessionScoped
public class VentaController implements Serializable {

    private List<String> years;
    
    private String monthViewCurrentYear;
    private String monthViewCurrentDistributor;
    private String monthViewSearch;
    
    private Date distributorViewStartDate;
    private Date distributorViewEndDate;
    private String distributorViewCurrentDistributor;
    private String distributorViewSearch;
    
    private Date compareViewStartDate;
    private Date compareViewEndDate;
    private String compareViewCurrentDistributor;
    private String compareViewCurrentDistributor2;
    private String compareViewSearch;
    
    private double totalSalesYear = 0.0;
    private double totalSalesActualMonth = 0.0;
    
    private DataModel totalSalesMonth = null;
    private DataModel totalSalesDistributorYear = null;
    private DataModel totalSalesDistributorCompare = null;
    private DataModel totalSalesDistributorCompare2 = null;
    private DataModel totalDifference = null;
    
    private GoogleChartModel chartModelCurrentYear = new DefaultGoogleChartModel("LineChart");
    private GoogleChartModel chartModelPastYears = new DefaultGoogleChartModel("LineChart");
    private String graphViewCurrentYear;
    
    @EJB
    private session.VentaFacade ejbFacade;
    
    private double pendienteCobrarAnual = 0.0;
    private String pendienteCobrarViewCurrentDistributor;
    private String pendienteCobrarViewSearch;
    private DataModel pendienteCobrarDistribuidor = null;
    
    
    
    
    
    private Venta current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    
    public VentaController() {
        this.monthViewCurrentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        this.monthViewCurrentDistributor = "-1";
        
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 01);
        this.distributorViewStartDate = cal1.getTime();
        this.compareViewStartDate = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
        this.distributorViewEndDate =  cal2.getTime();
        this.compareViewEndDate =  cal2.getTime();
        this.distributorViewCurrentDistributor = "-1";
        this.compareViewCurrentDistributor = "-1";
        this.compareViewCurrentDistributor2 = "-1";
        
        this.graphViewCurrentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        
        this.pendienteCobrarViewCurrentDistributor = "-1";
    }
    
    @PostConstruct
    public void init() {
        this.totalSalesMonth = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(monthViewCurrentYear), -1));
        this.totalSalesDistributorYear = new ListDataModel(this.getFacade().getDistributorSales(distributorViewStartDate, distributorViewEndDate, -1));
        List<VentaMes> distributor1 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, -1);
        List<VentaMes> distributor2 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, -1);
        List<VentaMes> difference = new ArrayList<VentaMes>();
        this.totalSalesDistributorCompare = new ListDataModel(distributor1);
        this.totalSalesDistributorCompare2 = new ListDataModel(distributor2);
        for (int i = 0; i < distributor1.size(); i++) {
            VentaMes diff = new VentaMes();
            diff.setTotal(distributor1.get(i).getTotal() - distributor2.get(i).getTotal());
            difference.add(diff);
        }
        this.totalDifference = new ListDataModel(difference);
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear + ", distribuidor Todos";
        this.distributorViewSearch = "Busqueda entre " +  df.format(distributorViewStartDate) + " y " + df.format(distributorViewEndDate) + ", distribuidor Todos";
        this.compareViewSearch = "Busqueda entre " +  df.format(compareViewStartDate) + " y " + df.format(compareViewEndDate) + ", comparando el distribuidor Todos con el distribuidor Todos";
        
        this.pendienteCobrarDistribuidor = new ListDataModel(this.getFacade().getPendienteCobrar(-1));
        this.pendienteCobrarViewSearch = "Busqueda por distribuidor Todos";        
        
        loadGraphMonths(graphViewCurrentYear);
        loadGraphYears();
    }
    
    public String getMonthViewCurrentYear(){
        return monthViewCurrentYear;
    }
    
    public String getMonthViewCurrentDistributor(){
        return monthViewCurrentDistributor;
    }
    
    public String getMonthViewSearch(){
        return monthViewSearch;
    }

    public Date getDistributorViewStartDate() {
        return distributorViewStartDate;
    }

    public Date getDistributorViewEndDate() {
        return distributorViewEndDate;
    }

    public String getDistributorViewCurrentDistributor() {
        return distributorViewCurrentDistributor;
    }

    public String getDistributorViewSearch() {
        return distributorViewSearch;
    }

    public Date getCompareViewStartDate() {
        return compareViewStartDate;
    }

    public Date getCompareViewEndDate() {
        return compareViewEndDate;
    }

    public String getCompareViewCurrentDistributor() {
        return compareViewCurrentDistributor;
    }

    public String getCompareViewCurrentDistributor2() {
        return compareViewCurrentDistributor2;
    }

    public String getCompareViewSearch() {
        return compareViewSearch;
    }

    public String getPendienteCobrarViewCurrentDistributor() {
        return pendienteCobrarViewCurrentDistributor;
    }

    public String getPendienteCobrarViewSearch() {
        return pendienteCobrarViewSearch;
    }
    
    public void setMonthViewCurrentYear(String monthViewCurrentYear){
        this.monthViewCurrentYear = monthViewCurrentYear;
    }
    
    public void setMonthViewCurrentDistributor(String monthViewCurrentDistributor){
        this.monthViewCurrentDistributor = monthViewCurrentDistributor;
    }

    public void setDistributorViewStartDate(Date distributorViewStartDate) {
        this.distributorViewStartDate = distributorViewStartDate;
    }

    public void setDistributorViewEndDate(Date distributorViewEndDate) {
        this.distributorViewEndDate = distributorViewEndDate;
    }

    public void setDistributorViewCurrentDistributor(String distributorViewCurrentDistributor) {
        this.distributorViewCurrentDistributor = distributorViewCurrentDistributor;
    }

    public void setCompareViewStartDate(Date compareViewStartDate) {
        this.compareViewStartDate = compareViewStartDate;
    }

    public void setCompareViewEndDate(Date compareViewEndDate) {
        this.compareViewEndDate = compareViewEndDate;
    }

    public void setCompareViewCurrentDistributor(String compareViewCurrentDistributor) {
        this.compareViewCurrentDistributor = compareViewCurrentDistributor;
    }

    public void setCompareViewCurrentDistributor2(String compareViewCurrentDistributor2) {
        this.compareViewCurrentDistributor2 = compareViewCurrentDistributor2;
    }

    public void setPendienteCobrarViewCurrentDistributor(String pendienteCobrarViewCurrentDistributor) {
        this.pendienteCobrarViewCurrentDistributor = pendienteCobrarViewCurrentDistributor;
    }
    
    public List<String> getYears() {
        this.years = this.getFacade().getYears();
        return years;
    }
    
    /*
        Generales
    */
    public double getTotalSalesYear(){
        this.totalSalesYear  = this.getFacade().getTotalSalesYear(Calendar.getInstance().get(Calendar.YEAR));
        return this.totalSalesYear;
    }
    
    public double getTotalSalesActualMonth(){
        this.totalSalesActualMonth  = this.getFacade().getTotalSalesActualMonth(Calendar.getInstance().get(Calendar.MONTH));
        return this.totalSalesActualMonth;
    }
    
    public double getPendienteCobrarAnual(){
        this.pendienteCobrarAnual = this.getFacade().getPendienteCobrarAnual();
        return this.pendienteCobrarAnual;
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
    
    public DataModel getTotalDifference() {
        return totalDifference;
    }
    
    public DataModel getPendienteCobrarDistribuidor() {
        return pendienteCobrarDistribuidor;
    }
    
    public String searchMonth()
    {
        this.totalSalesMonth = new ListDataModel(this.getFacade().getMonthSales(Integer.parseInt(monthViewCurrentYear), Integer.parseInt(monthViewCurrentDistributor)));
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear +
                               ", distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(monthViewCurrentDistributor));        
        return "";
    }
    
    public String searchDistributor()
    {
        this.totalSalesDistributorYear = new ListDataModel(this.getFacade().getDistributorSales(distributorViewStartDate, distributorViewEndDate, Integer.parseInt(distributorViewCurrentDistributor)));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.distributorViewSearch = "Busqueda entre " +  df.format(distributorViewStartDate) + " y " + df.format(distributorViewEndDate) +
                                     ", distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(distributorViewCurrentDistributor));        
        return "";
    }

    public String compareDistributor()
    {
        List<VentaMes> distributor1 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, Integer.parseInt(compareViewCurrentDistributor));
        List<VentaMes> distributor2 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, Integer.parseInt(compareViewCurrentDistributor2));
        List<VentaMes> difference = new ArrayList<VentaMes>();
        for (int i = 0; i < distributor1.size(); i++) {
            VentaMes diff = new VentaMes();
            diff.setTotal(distributor1.get(i).getTotal() - distributor2.get(i).getTotal());
            difference.add(diff);
        }
        this.totalSalesDistributorCompare = new ListDataModel(distributor1);
        this.totalSalesDistributorCompare2 = new ListDataModel(distributor2);
        this.totalDifference = new ListDataModel(difference);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.compareViewSearch = "Busqueda entre " +  df.format(compareViewStartDate) + " y " + df.format(compareViewEndDate) + 
                                 ", comparando el distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(compareViewCurrentDistributor)) + 
                                " con el distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(compareViewCurrentDistributor2));
        return "";
    }

    public String searchPendienteCobrar()
    {
        this.pendienteCobrarDistribuidor = new ListDataModel(this.getFacade().getPendienteCobrar(Integer.parseInt(pendienteCobrarViewCurrentDistributor)));
        this.pendienteCobrarViewSearch = "Busqueda por distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(pendienteCobrarViewCurrentDistributor));        
        return "";
    }
    
    
    
    
    
    
    
    
    
    
    
    //
    
    /*
     * Gráficas
     */
    
    public String getGraphViewCurrentYear() {
        return graphViewCurrentYear;
    }
    
    public void setGraphViewCurrentYear(String graphViewCurrentYear) {
        this.graphViewCurrentYear = graphViewCurrentYear;
    }
    
    private void loadGraphMonths(String year) {
        List<VentaMes> totalSales = this.getFacade().getMonthSales(Integer.parseInt(year), -1);
        chartModelCurrentYear.addColumn(new Column(Column.JavaScriptType.string, "Mes"));
        chartModelCurrentYear.addColumn(new Column(Column.JavaScriptType.number, "Ventas"));
        
        DecimalFormat df = new DecimalFormat("#,###,###,###.##");
        int noOfRows = 2;
        for(VentaMes n: totalSales) {
            Row row = new Row(noOfRows);
            row.addEntry("'" + n.getMes() + "'");
            
            row.addEntry("{ v: " + String.valueOf(n.getTotal()) + ", f: '$" + df.format(n.getTotal()) + "'}");
            chartModelCurrentYear.addRow(row);
        }
        
        chartModelCurrentYear.setOptions("'title':'" + year + "', 'displayAnnotations':false"); // Simply inserted as javascript.
    }
    
    private void loadGraphYears( ) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        chartModelPastYears.addColumn(new Column(Column.JavaScriptType.string, "Año"));
        chartModelPastYears.addColumn(new Column(Column.JavaScriptType.number, "Ventas"));
        
        DecimalFormat df = new DecimalFormat("#,###,###,###.##");
        int noOfRows = 2;
        for(int i=-10; i < 1; i++) {
            int y = currentYear + i;
            Row row = new Row(noOfRows);
            row.addEntry("'" + y + "'");
            double amount = this.getFacade().getTotalSalesYear(y);
            row.addEntry("{ v: " + amount + ", f: '$" + df.format(amount) + "'}");
            chartModelPastYears.addRow(row);
            
        }
        
        chartModelPastYears.setOptions("title:'Ventas Historicas', displayAnnotations:false"); // Simply inserted as javascript.  

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
