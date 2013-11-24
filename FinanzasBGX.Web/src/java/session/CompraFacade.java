/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Compra;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import utilidades.Column;
import utilidades.DateUtils;
import utilidades.NumericTableSet;
import utilidades.NumericTreeRow;

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
        Devuelve un arreglo de 12 elementos Double. Se obtiene el costo total de las compras
        de cada mes en el año especificado, donde cada mes esta enumerado
        en el arreglo de 0 a 11 (enero...diciembre)
    */
    public List<Column> getMonthPurchases(int year, String month) {
        NumericTableSet acum = new NumericTableSet();
        for (Compra c : this.C()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                String strMonth = DateUtils.getMonth(Calendar.MONTH);
                if(strMonth.equals(month) || month.equals("*")){
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
    public List<Column> getSupplierPurchases(int year) {
        NumericTableSet acum = new NumericTableSet();
        for (Compra c : this.C()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(c.getFecPago());
            if (cal.get(Calendar.YEAR) == year) {
                String proveedor = c.getProveedorId().getId().toString();
                double total = c.getCostoTotal();
                acum.addValue(proveedor, total);
            }
        }
        return acum.getSumRow().getDescendingColumns();
    }
    
}
