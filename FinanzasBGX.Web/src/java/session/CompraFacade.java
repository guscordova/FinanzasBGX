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
import entities.Pedido;
import entities.PedidoComponente;
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
            if (c.getEstatus() == 1) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(c.getFecPago());
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
    
    public double getTotalPurchasesYear(int year) {
        double purchasesSum = 0;
        for (Compra c : findAll()) {
            if (c.getEstatus() == 1) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(c.getFecPago());
                if (cal.get(Calendar.YEAR) == year) {
                    purchasesSum += c.getCostoTotal();
                }
            }
        }
        return purchasesSum;
    }
    
    public double getTotalPurchasesActualMonth(int month) {
        double purchasesSum = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (Compra c : findAll()) {
            if (c.getEstatus() == 1) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(c.getFecPago());
                if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                    purchasesSum += c.getCostoTotal();
                }
            }
        }
        return purchasesSum;
    }
    
    public List<CompraMes> getMonthPurchases(int year, int supplier, int component) {
        List<CompraMes> purchasesSum = new ArrayList<CompraMes>();
        purchasesSum.add(new CompraMes ("Enero", 0));
        purchasesSum.add(new CompraMes ("Febrero", 1));
        purchasesSum.add(new CompraMes ("Marzo", 2));
        purchasesSum.add(new CompraMes ("Abril", 3));
        purchasesSum.add(new CompraMes ("Mayo", 4));
        purchasesSum.add(new CompraMes ("Junio", 5));
        purchasesSum.add(new CompraMes ("Julio", 6));
        purchasesSum.add(new CompraMes ("Agosto", 7));
        purchasesSum.add(new CompraMes ("Septiembre", 8));
        purchasesSum.add(new CompraMes ("Octubre", 9));
        purchasesSum.add(new CompraMes ("Noviembre", 10));
        purchasesSum.add(new CompraMes ("Diciembre", 11));
        for (Compra c : findAll()) {
            if (c.getEstatus() == 1) { 
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
                            for (CompraComponente cc : c.getCompraComponenteCollection()) {
                                if (cc.getComponente().getId() == component) {
                                    CompraMes m = purchasesSum.get(cal.get(Calendar.MONTH));
                                    m.setTotal(cc.getCantidad() * cc.getCosto() + m.getTotal());
                                }
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
            if (c.getEstatus() == 1) {
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
                            for (CompraComponente cc : c.getCompraComponenteCollection()) {
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
        }
        return purchasesSum;
    }

    public List<CompraComponentes> getComponentPurchases(Date startDate, Date endDate, int supplier, int component) {
        List<CompraComponentes> purchasesSum = new ArrayList<CompraComponentes>();
        for (Compra c : findAll()) {
            if (c.getEstatus() == 1) {
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
                        for (CompraComponente cc : c.getCompraComponenteCollection()) {
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
    
    
    public List<CompraComponentes> getInventario(int idComponent) {
        List<CompraComponentes> inventario = new ArrayList<CompraComponentes>();
        for (Compra c : findAll()) {
            if (c.getEstatus() == 1) {
                for (CompraComponente cc : c.getCompraComponenteCollection()) {
                    if (idComponent == - 1 || cc.getComponente().getId() == idComponent) {
                        CompraComponentes componente = null;
                        for (CompraComponentes com : inventario) {
                            if (com.getId() == cc.getComponente().getId())
                            {
                                componente = com;
                                break;
                            }
                        }
                        if (componente == null) {
                            componente = new CompraComponentes();
                            componente.setId(cc.getComponente().getId());
                            componente.setComponente(cc.getComponente().getNombre());
                            componente.setCantidad(cc.getCantidad());
                            inventario.add(componente);
                        }
                        else {
                            componente.setCantidad(cc.getCantidad() + componente.getCantidad());
                        }
                    }
                }
            }
        }
        for (Pedido pedido : Pedido()) {
            if (pedido.getEstatus() == 1) {
                for (PedidoComponente pc : pedido.getPedidoComponenteCollection()) {
                    if (idComponent == - 1 || pc.getComponente().getId() == idComponent) {
                        CompraComponentes componente = null;
                        for (CompraComponentes com : inventario) {
                            if (com.getId() == pc.getComponente().getId())
                            {
                                componente = com;
                                break;
                            }
                        }
                        if (componente != null) {
                            componente.setCantidad(componente.getCantidad() - pc.getCantidad());
                            if (componente.getCantidad() <= 0) 
                                inventario.remove(componente);
                        }
                    }
                }
            }
        }
        return inventario;
    }
    
    public List<Pedido> Pedido(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Pedido> pedido = cq.from(Pedido.class);
        cq.select(pedido);
        return this.em.createQuery(cq).getResultList();
    }
}
