/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import dto.CompraComponentes;
import dto.CompraMes;
import dto.CompraProveedor;
import entities.Componente;
import entities.Compra;
import entities.CompraComponente;
import entities.Proveedor;
import entities.ProveedorComponente;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import utilidades.Column;
import utilidades.CompraDB;
import utilidades.DateUtils;
import utilidades.NumericTableSet;

/**
 *
 * @author robertogarza
 */
@Stateless
public class CompraFacade extends AbstractFacade<Compra> {
    @PersistenceContext(unitName = "FinanzasBGX.WebPU")
    private EntityManager em;

    public CompraFacade() {
        super(Compra.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<String> getYears() {
        List<String> years = new ArrayList<String>();
        int maxYear = Calendar.getInstance().get(Calendar.YEAR);
        int minYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) > maxYear)  
                maxYear = cal.get(Calendar.YEAR);
            if (cal.get(Calendar.YEAR) < minYear)  
                minYear = cal.get(Calendar.YEAR);
        }
        while (maxYear >= minYear) {
            years.add(maxYear + "");
            maxYear--;
        }
        return years;
    }
    
    public double getTotalPurchasesYear(int year) {
        double purchasesSum = 0;
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                purchasesSum += c.getCostoTotal();
            }
        }
        return purchasesSum;
    }
    
    public double getTotalPurchasesActualMonth(int month) {
        double purchasesSum = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                purchasesSum += c.getCostoTotal();
            }
        }
        return purchasesSum;
    }
    
    public List<CompraMes> getMonthPurchases(int year, int supplier, int component) {
        List<CompraMes> purchasesSum = new ArrayList<CompraMes>();
        purchasesSum.add(new CompraMes ("Enero"));
        purchasesSum.add(new CompraMes ("Febrero"));
        purchasesSum.add(new CompraMes ("Marzo"));
        purchasesSum.add(new CompraMes ("Abril"));
        purchasesSum.add(new CompraMes ("Mayo"));
        purchasesSum.add(new CompraMes ("Junio"));
        purchasesSum.add(new CompraMes ("Julio"));
        purchasesSum.add(new CompraMes ("Agosto"));
        purchasesSum.add(new CompraMes ("Septiembre"));
        purchasesSum.add(new CompraMes ("Octubre"));
        purchasesSum.add(new CompraMes ("Noviembre"));
        purchasesSum.add(new CompraMes ("Diciembre"));
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                if (supplier == -1 || c.getProveedorId().getId() == supplier) 
                {
                    if (component == -1) {
                        CompraMes m = purchasesSum.get(cal.get(Calendar.MONTH));
                        m.setTotal(c.getCostoTotal() + m.getTotal());
                    }
                    else {
                        Iterator<CompraComponente> it = c.getCompraComponenteCollection().iterator();
                        while(it.hasNext()){
                            CompraComponente cc = it.next();
                            if (cc.getComponente().getId() == component) {
                                CompraMes m = purchasesSum.get(cal.get(Calendar.MONTH));
                                m.setTotal(cc.getCantidad() * cc.getCosto() + m.getTotal());
                            }
                        }
                    }
                }
            }
        }
        return purchasesSum;
    }
    
    public List<CompraProveedor> getSupplierPurchases(Date startDate, Date endDate, int supplier, int component) {
        List<CompraProveedor> purchasesSum = new ArrayList<CompraProveedor>();
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.getTime().after(startDate) && cal.getTime().before(endDate)) {
                if (supplier == -1 || c.getProveedorId().getId() == supplier) 
                {
                    CompraProveedor proveedor = null;
                    for (CompraProveedor p : purchasesSum) {
                        if (p.getId() == c.getProveedorId().getId())
                        {
                            proveedor = p;
                            break;
                        }
                    }
                    if (component == -1) {
                        if (proveedor == null) {
                            proveedor = new CompraProveedor();
                            proveedor.setId(c.getProveedorId().getId());
                            proveedor.setProveedor(c.getProveedorId().getRazonSocial());
                            proveedor.setTotal(c.getCostoTotal());
                            purchasesSum.add(proveedor);
                        }
                        else {
                            proveedor.setTotal(c.getCostoTotal() + proveedor.getTotal());
                        }
                    }
                    else {
                        Iterator<CompraComponente> it = c.getCompraComponenteCollection().iterator();
                        while(it.hasNext()){
                            CompraComponente cc = it.next();
                            if (cc.getComponente().getId() == component) {
                                if (proveedor == null) {
                                    proveedor = new CompraProveedor();
                                    proveedor.setId(c.getProveedorId().getId());
                                    proveedor.setProveedor(c.getProveedorId().getRazonSocial());
                                    proveedor.setTotal(cc.getCantidad() * cc.getCosto());
                                    purchasesSum.add(proveedor);
                                }
                                else {
                                    proveedor.setTotal(cc.getCantidad() * cc.getCosto() + proveedor.getTotal());
                                }
                            }
                        }
                    }
                }
            }
        }
        return purchasesSum;
    }

    public List<CompraComponentes> getComponentPurchases(Date startDate, Date endDate, int supplier, int component) {
        List<CompraComponentes> purchasesSum = new ArrayList<CompraComponentes>();
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.getTime().after(startDate) && cal.getTime().before(endDate)) {
                if (supplier == -1 || c.getProveedorId().getId() == supplier) 
                {
                    CompraComponentes componente = null;
                    for (CompraComponentes com : purchasesSum) {
                        if (com.getId() == c.getProveedorId().getId())
                        {
                            componente = com;
                            break;
                        }
                    }
                    Iterator<CompraComponente> it = c.getCompraComponenteCollection().iterator();
                    while(it.hasNext()){
                        CompraComponente cc = it.next();
                        if (component == - 1 || cc.getComponente().getId() == component) {
                            if (componente == null) {
                                componente = new CompraComponentes();
                                componente.setId(cc.getComponente().getId());
                                componente.setComponente(cc.getComponente().getNombre());
                                componente.setTotal(cc.getCantidad() * cc.getCosto());
                                purchasesSum.add(componente);
                            }
                            else {
                                componente.setTotal(cc.getCantidad() * cc.getCosto() + componente.getTotal());
                            }
                        }
                    }
                }
            }
        }
        return purchasesSum;
    }
    
    
    public String getSupplierNameById(int idSupplier) {
        if (idSupplier != -1) {
            javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
            Root<Proveedor> supplier = cq.from(Proveedor.class);
            cq.select(supplier);
            List<Proveedor> suppliers = this.em.createQuery(cq).getResultList();
            for (Proveedor p : suppliers) {
                if (p.getId() == idSupplier)
                    return p.getRazonSocial();
            }
        }
        return "Todos";
    }
    
    public String getComponentNameById(int idComponent) {
        if (idComponent != -1) {
            javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
            Root<Componente> component = cq.from(Componente.class);
            cq.select(component);
            List<Componente> components = this.em.createQuery(cq).getResultList();
            for (Componente c : components) {
                if (c.getId() == idComponent)
                    return c.getNombre();
            }
        }
        return "Todos";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public CompraDB getCompraDB(int year){
        CompraDB cdb = new CompraDB(year);
        for (Compra c : findAll()) {
            cdb.add(c);
        }
        return cdb;
    }
    
    
    /*
        Calcula total comprado por año
    */
    public double getYearPurchases(int year) {
        double acum = 0;
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                acum += c.getCostoTotal();
            }
        }
        return acum;
    }
    
    /*
        Calcula total comprado por mes
    */
    public List<Column> getMonthPurchases(int year) {
        NumericTableSet acum = DateUtils.getDatedTableSet();
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                double total = c.getCostoTotal();
                acum.addValue(strMonth, total);
            }
        }
        return acum.getSumRow().getDescendingColumns();
    }
    
    /*
        Devuelve un HashMap, donde la llave es el nombre del proveedor y el valor
        asociado a la llave es el total de las compras en el año especificado para ese
        proveedor
    */
    public List<Column> getSupplierPurchases(int year, String selectedSupplier) {
        NumericTableSet acum = new NumericTableSet();
        for (Compra c : findAll()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                Proveedor proveedor = c.getProveedorId();
                String proveedorID = proveedor.getId().toString();
                if(selectedSupplier.equals(proveedorID) || selectedSupplier.equals("*")){
                    String proveedorName = proveedor.getEmail();
                    String key = proveedorID + " " + proveedorName;
                    double total = c.getCostoTotal();
                    acum.addValue(key, total);
                }
            }
        }
        return acum.getSumRow().getDescendingColumns();
    }
    
}
