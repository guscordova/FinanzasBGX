/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import dto.ModeloPorProducir;
import entities.Cliente;
import entities.Estatus;
import entities.Orden;
import entities.Venta;
import dto.VentaDistribuidor;
import dto.VentaMes;
import entities.OrdenModelo;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import utilidades.Column;
import utilidades.DateUtils;
import utilidades.NumericTableSet;
import utilidades.NumericTreeRow;
import utilidades.Record;

/*
    Implementado:
        Calcula total vendido por año
        Calcula total vendido por mes
        Calcula total vendido por distribuidor
        Comparar distribuidores
        Calcula pendiente por cobrar
        Calcula pendiente por cobrar por distribuidor
        Calcula pendiente por producir
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;
    //
    
    public VentaFacade() {
        super(Venta.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /*
        Recaba todas las ordenes realizadas en un año dado
    */
    public List<Orden> O(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Orden> orden = cq.from(Orden.class);
        cq.select(orden);
        return this.em.createQuery(cq).getResultList();
    }
    
    /*
        Recaba un cliente dado su id (distribuidor)
    */
    public Cliente CById(Integer clientID){
        return this.em.find(Cliente.class, clientID);
    }
    
    /*
        Venta Controller functions
    */
    public List<String> getYears() {
        List<String> years = new ArrayList<String>();
        int maxYear = Calendar.getInstance().get(Calendar.YEAR);
        int minYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) > maxYear)  
                    maxYear = cal.get(Calendar.YEAR);
                if (cal.get(Calendar.YEAR) < minYear)  
                    minYear = cal.get(Calendar.YEAR);
            }
        }
        while (maxYear >= minYear) {
            years.add(maxYear + "");
            maxYear--;
        }
        return years;
    }
    /*
        Calcula total vendido por año
        Obtiene unicamente las ventas del año especificado y obtiene su sumatoria
    */
    public double getTotalSalesYear(int year) {
        double salesSum = 0;
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) == year) {
                    salesSum += v.getMonto() * v.getCantidad();
                }
            }
        }
        return salesSum;
    }
    
    public double getTotalSalesActualMonth(int month) {
        double salesSum = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                    salesSum += v.getMonto() * v.getCantidad();
                }
            }
        }
        return salesSum;
    }
    
    
    /*
        Calcula total vendido por mes
        Obtiene unicamente las ventas del año especificado y organiza las 
        sumatoria por meses
    */
    public List<VentaMes> getMonthSales(int year, int distributor) {
        List<VentaMes> salesSum = new ArrayList<VentaMes>();
        salesSum.add(new VentaMes ("Enero"));
        salesSum.add(new VentaMes ("Febrero"));
        salesSum.add(new VentaMes ("Marzo"));
        salesSum.add(new VentaMes ("Abril"));
        salesSum.add(new VentaMes ("Mayo"));
        salesSum.add(new VentaMes ("Junio"));
        salesSum.add(new VentaMes ("Julio"));
        salesSum.add(new VentaMes ("Agosto"));
        salesSum.add(new VentaMes ("Septiembre"));
        salesSum.add(new VentaMes ("Octubre"));
        salesSum.add(new VentaMes ("Noviembre"));
        salesSum.add(new VentaMes ("Diciembre"));
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) == year) {
                    Cliente cliente = v.getOrdenId().getClienteId();
                    if (distributor == -1 || cliente.getId() == distributor) 
                    {
                        VentaMes m = salesSum.get(cal.get(Calendar.MONTH));
                        double total = v.getMonto() * v.getCantidad();
                        m.setTotal(total + m.getTotal());
                    }
                }
            }
        }
        return salesSum;
    }
    
    /*
        Calcula total vendido a distribuidores (auxiliar)
    */
    public List<VentaDistribuidor> getDistributorSales(Date startDate, Date endDate, int distributor) {
        List<VentaDistribuidor> salesAcum = new ArrayList<VentaDistribuidor>();
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.getTime().after(startDate) && cal.getTime().before(endDate)) {
                    Cliente cliente = v.getOrdenId().getClienteId();
                    if (distributor == -1 || cliente.getId() == distributor) {
                        VentaDistribuidor distribuidor = null;
                        for (VentaDistribuidor d : salesAcum) {
                            if (d.getId() == cliente.getId())
                            {
                                distribuidor = d;
                                break;
                            }
                        }
                        if (distribuidor == null)
                        {
                            distribuidor = new VentaDistribuidor();
                            distribuidor.setId(cliente.getId());
                            distribuidor.setDistribuidor(cliente.getNombre() + " " + cliente.getAppaterno() + " " + cliente.getApmaterno());
                            distribuidor.setTotal(v.getCantidad() * v.getMonto());
                            salesAcum.add(distribuidor);
                        }
                        else 
                            distribuidor.setTotal((v.getCantidad() * v.getMonto()) + distribuidor.getTotal());
                    }
                }
            }
        }
        return salesAcum;
    }
    
    public List<VentaMes> getMonthSalesByDate(Date startDate, Date endDate, int distributor) {
        List<VentaMes> salesSum = new ArrayList<VentaMes>();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        int monthStart = cal1.get(Calendar.MONTH);
        int yearStart = cal1.get(Calendar.YEAR);
        int monthEnd = cal2.get(Calendar.MONTH);
        int yearEnd = cal2.get(Calendar.YEAR);
        while (monthStart <= monthEnd && yearStart <= yearEnd) {
            VentaMes sale = new VentaMes();
            sale.setMonth(monthStart);
            sale.setYear(yearStart);
            switch (monthStart) {
                case 0: sale.setMes("Enero " + yearStart); break;
                case 1: sale.setMes("Febrero " + yearStart); break;
                case 2: sale.setMes("Marzo " + yearStart); break;
                case 3: sale.setMes("Abril " + yearStart); break;
                case 4: sale.setMes("Mayo " + yearStart); break;
                case 5: sale.setMes("Junio " + yearStart); break;
                case 6: sale.setMes("Julio " + yearStart); break;
                case 7: sale.setMes("Agosto " + yearStart); break;
                case 8: sale.setMes("Septiembre " + yearStart); break;
                case 9: sale.setMes("Octubre " + yearStart); break; 
                case 10: sale.setMes("Noviembre " + yearStart); break;
                case 11: sale.setMes("Diciembre " + yearStart); break;    
            }
            salesSum.add(sale);
            if (monthStart < 11)
                monthStart++;
            else {
                monthStart = 0;
                yearStart++;
            }
        }
        for (Venta v : findAll()) {
            if (v.getEstatus() != 0 && v.getFecCobro() != null && 
                    v.getOrdenId().getEstatusId().getId() != 1 && v.getOrdenId().getEstatusId().getId() != 4) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.getTime().after(startDate) && cal.getTime().before(endDate)) {
                    Cliente cliente = v.getOrdenId().getClienteId();
                    if (distributor == -1 || cliente.getId() == distributor) 
                    {
                        VentaMes sale = new VentaMes();
                        for(VentaMes m: salesSum) {
                            if (m.getMonth() == cal.get(Calendar.MONTH) && m.getYear() == cal.get(Calendar.YEAR))
                                sale = m;
                        }
                        double total = v.getMonto() * v.getCantidad();
                        sale.setTotal(total + sale.getTotal());
                    }
                }
            }
        }
        return salesSum;
    }
    
    
    public String getDistributorNameById(int idDistributor) {
        if (idDistributor != -1) {
            javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
            Root<Cliente> distributor = cq.from(Cliente.class);
            cq.select(distributor);
            List<Cliente> distributors = this.em.createQuery(cq).getResultList();
            for (Cliente p : distributors) {
                if (p.getId() == idDistributor)
                    return p.getNombre();
            }
        }
        return "Todos";
    }
    
    /*
        Calcula pendiente por cobrar total
    */
    public double getPendienteCobrarAnual() {
        double orderSum = 0;
        double saleSum = 0;
        for (Orden o : this.O()) {
            if (o.getEstatusId().getId() != 1 && o.getEstatusId().getId() != 4) {
                orderSum += o.getTotalPago();
                //  Ahora vemos los pagos que han sido realizados a esta orden
                //  y los descontamos al total
                for(Venta v : o.getVentaCollection()){
                    if (v.getEstatus() != 0 && v.getFecCobro() != null)
                        saleSum += v.getCantidad() * v.getMonto();
                }
            }
        }
        return orderSum - saleSum;
    }
    
    public List<VentaDistribuidor> getPendienteCobrar(int idDistributor) {
        List<VentaDistribuidor> pendientes = new ArrayList<VentaDistribuidor>();
        for (Orden o : this.O()) {
            if (idDistributor == -1 || o.getClienteId().getId() == idDistributor) {
                if (o.getEstatusId().getId() != 1 && o.getEstatusId().getId() != 4) {
                    double saleSum = 0;
                    for(Venta v : o.getVentaCollection()){
                        if (v.getEstatus() != 0 && v.getFecCobro() != null)
                            saleSum += v.getCantidad() * v.getMonto();
                    }
                    double difference = o.getTotalPago() - saleSum;
                    if (difference != 0) {
                        VentaDistribuidor pendiente = null;
                        for (VentaDistribuidor p : pendientes) {
                            if (p.getId() == o.getClienteId().getId())
                            {
                                pendiente = p;
                                break;
                            }
                        }
                        if (pendiente == null)
                        {
                            pendiente = new VentaDistribuidor();
                            pendiente.setId(o.getClienteId().getId());
                            pendiente.setDistribuidor(o.getClienteId().getNombre() + " " + o.getClienteId().getAppaterno() + " " + o.getClienteId().getApmaterno());
                            pendiente.setTotal(difference);
                            pendientes.add(pendiente);
                        }
                        else 
                            pendiente.setTotal(difference + pendiente.getTotal());
                    }
                }
            }
        }
        return pendientes;
    }
    
    public double getPendienteProducirAnual() {
        double producirSum = 0;
        for (OrdenModelo o : this.OrdenModelo()) {
            if (o.getOrden().getEstatusId().getId() != 1 && o.getOrden().getEstatusId().getId() != 4) {
                producirSum += ((o.getCantidad() - o.getFabricadas()) * o.getModelo().getPrecioVenta());
            }
        }
        return producirSum;
    }
    
    public List<ModeloPorProducir> getPendienteProducir(int idModelo) {
        List<ModeloPorProducir> porProducir = new ArrayList<ModeloPorProducir>();
        
        return porProducir;
    }
    
    public List<OrdenModelo> OrdenModelo(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Orden> ordenModelo = cq.from(OrdenModelo.class);
        cq.select(ordenModelo);
        return this.em.createQuery(cq).getResultList();
    }
}
    