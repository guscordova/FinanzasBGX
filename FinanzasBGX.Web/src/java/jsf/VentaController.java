package jsf;

import dto.ModeloPorProducir;
import dto.VentaDistribuidor;
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
    
    private List<VentaMes> totalSalesMonth = null;
    private List<VentaDistribuidor> totalSalesDistributorYear = null;
    private List<VentaMes> totalSalesDistributorCompare = null;
    private List<VentaMes> totalSalesDistributorCompare2 = null;
    private List<VentaMes> totalDifference = null;
    
    private GoogleChartModel chartModelCurrentYear = new DefaultGoogleChartModel("LineChart");
    private GoogleChartModel chartModelPastYears = new DefaultGoogleChartModel("LineChart");
    private String graphYear;
    private String graphViewCurrentYear;
    
    @EJB
    private session.VentaFacade ejbFacade;
    
    private double pendienteCobrarAnual = 0.0;
    private String pendienteCobrarViewCurrentDistributor;
    private String pendienteCobrarViewSearch;
    private List<VentaDistribuidor> pendienteCobrarDistribuidor = null;
    
    private double pendienteProducirAnual = 0.0;
    private String pendienteProducirViewCurrentModel;
    private String pendienteProducirViewSearch;
    private List<ModeloPorProducir> pendienteProducirComponent = null;
    
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
        
        this.pendienteProducirViewCurrentModel = "-1";
    }
    
    @PostConstruct
    public void init() {
        this.totalSalesMonth = this.getFacade().getMonthSales(Integer.parseInt(monthViewCurrentYear), -1);
        this.totalSalesDistributorYear = this.getFacade().getDistributorSales(distributorViewStartDate, distributorViewEndDate, -1);
        List<VentaMes> distributor1 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, -1);
        List<VentaMes> distributor2 = this.getFacade().getMonthSalesByDate(compareViewStartDate, compareViewEndDate, -1);
        List<VentaMes> difference = new ArrayList<VentaMes>();
        this.totalSalesDistributorCompare = distributor1;
        this.totalSalesDistributorCompare2 = distributor2;
        for (int i = 0; i < distributor1.size(); i++) {
            VentaMes diff = new VentaMes();
            diff.setTotal(distributor1.get(i).getTotal() - distributor2.get(i).getTotal());
            difference.add(diff);
        }
        this.totalDifference = difference;
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear + ", distribuidor Todos";
        this.distributorViewSearch = "Busqueda entre " +  df.format(distributorViewStartDate) + " y " + df.format(distributorViewEndDate) + ", distribuidor Todos";
        this.compareViewSearch = "Busqueda entre " +  df.format(compareViewStartDate) + " y " + df.format(compareViewEndDate) + ", comparando el distribuidor Todos con el distribuidor Todos";
        
        this.pendienteCobrarDistribuidor = this.getFacade().getPendienteCobrar(-1);
        this.pendienteCobrarViewSearch = "Busqueda por distribuidor Todos";        
        
        this.pendienteProducirComponent = this.getFacade().getPendienteProducir(-1);
        this.pendienteProducirViewSearch = "Busqueda por modelo Todos";
        
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

    public String getPendienteProducirViewCurrentModel() {
        return pendienteProducirViewCurrentModel;
    }

    public String getPendienteProducirViewSearch() {
        return pendienteProducirViewSearch;
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

    public void setPendienteProducirViewCurrentModel(String pendienteProducirViewCurrentModel) {
        this.pendienteProducirViewCurrentModel = pendienteProducirViewCurrentModel;
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
    
    public double getPendienteProducirAnual(){
        this.pendienteProducirAnual = this.getFacade().getPendienteProducirAnual();
        return this.pendienteProducirAnual;
    }

    public List<VentaMes> getTotalSalesMonth(){
        return totalSalesMonth;
    }
    
    public List<VentaDistribuidor> getTotalSalesDistributorYear(){
        return totalSalesDistributorYear;
    }
    
    public List<VentaMes> getTotalSalesDistributorCompare(){
        return totalSalesDistributorCompare;
    }
    
    public List<VentaMes> getTotalSalesDistributorCompare2(){
        return totalSalesDistributorCompare2;
    }
    
    public List<VentaMes> getTotalDifference() {
        return totalDifference;
    }
    
    public List<VentaDistribuidor> getPendienteCobrarDistribuidor() {
        return pendienteCobrarDistribuidor;
    }
    
    public List<ModeloPorProducir> getPendienteProducirComponent() {
        return pendienteProducirComponent;
    }
    
    public String searchMonth()
    {
        this.totalSalesMonth = this.getFacade().getMonthSales(Integer.parseInt(monthViewCurrentYear), Integer.parseInt(monthViewCurrentDistributor));
        this.monthViewSearch = "Busqueda por año " + monthViewCurrentYear +
                               ", distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(monthViewCurrentDistributor));        
        return "";
    }
    
    public String searchDistributor()
    {
        this.totalSalesDistributorYear = this.getFacade().getDistributorSales(distributorViewStartDate, distributorViewEndDate, Integer.parseInt(distributorViewCurrentDistributor));
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
        this.totalSalesDistributorCompare = distributor1;
        this.totalSalesDistributorCompare2 = distributor2;
        this.totalDifference = difference;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.compareViewSearch = "Busqueda entre " +  df.format(compareViewStartDate) + " y " + df.format(compareViewEndDate) + 
                                 ", comparando el distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(compareViewCurrentDistributor)) + 
                                " con el distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(compareViewCurrentDistributor2));
        return "";
    }

    public String searchPendienteCobrar()
    {
        this.pendienteCobrarDistribuidor = this.getFacade().getPendienteCobrar(Integer.parseInt(pendienteCobrarViewCurrentDistributor));
        this.pendienteCobrarViewSearch = "Busqueda por distribuidor " + this.getFacade().getDistributorNameById(Integer.parseInt(pendienteCobrarViewCurrentDistributor));        
        return "";
    }
    
    public String searchPendienteProducir() {
        this.pendienteProducirComponent = this.getFacade().getPendienteProducir(Integer.parseInt(pendienteProducirViewCurrentModel));
        this.pendienteProducirViewSearch = "Busqueda por modelo " + this.getFacade().getModelNameById(Integer.parseInt(pendienteProducirViewCurrentModel));   
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
    
    public String getGraphYear() {
        return graphYear;
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
        this.graphYear = "Busqueda por año " + year;
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
    
     private VentaFacade getFacade() {
        return ejbFacade;
    }
}
