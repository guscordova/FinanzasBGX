/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Compra;
import entities.Proveedor;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import utilidades.Column;
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

    /*
        Realiza una consulta, devuelve todas las compras de la base de datos,
        el universo de compras C
    */
    public List<Compra> C(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Compra> compra = cq.from(Compra.class);
        cq.select(compra);
        return this.em.createQuery(cq).getResultList();
    }
    
    /*
        Suma el costo total de todas las compras que pertenezcan
        al año especificado como parametro
    */
    public double getYearPurchases(int year) {
        double acum = 0;
        for (Compra c : this.C()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                acum += c.getCostoTotal();
            }
        }
        return acum;
    }
    
    /*
        Por mes
    */
    public List<Column> getMonthPurchases(int year, String month) {
        NumericTableSet acum = DateUtils.getDatedTableSet();
        for (Compra c : this.C()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                if(month.equals(strMonth) || month.equals("*")){
                    double total = c.getCostoTotal();
                    acum.addValue(strMonth, total);
                }
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
        for (Compra c : this.C()) {
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
