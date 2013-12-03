/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Cliente;
import entities.Estatus;
import entities.Orden;
import entities.Venta;
import dto.VentaDistribuidor;
import dto.VentaMes;
import java.util.ArrayList;
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
        Recaba todas las ventas de un año dado
    */
    public List<Venta> V(){
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        Root<Venta> venta = cq.from(Venta.class);
        cq.select(venta);
        return this.em.createQuery(cq).getResultList();
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
    
    /*
        Calcula total vendido por año
        Obtiene unicamente las ventas del año especificado y obtiene su sumatoria
    */
    public double getTotalSalesYear(int year) {
        double salesSum = 0;
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
                salesSum += v.getMonto() * v.getCantidad();
            }
        }
        return salesSum;
    }
    
    public double getTotalSalesActualMonth(int month) {
        double salesSum = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                salesSum += v.getMonto() * v.getCantidad();
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
        for (Venta v : this.V()) {
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
        return salesSum;
    }
    
    /*
        Calcula total vendido a distribuidores (auxiliar)
    */
    public List<VentaDistribuidor> getDistributorSalesTable(int year, int distributor) {
        List<VentaDistribuidor> salesAcum = new ArrayList<VentaDistribuidor>();
        for (Venta v : this.V()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(v.getFecCobro());
            if (cal.get(Calendar.YEAR) == year) {
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
        return salesAcum;
    }
   
    /*
       Comparar distribuidores (auxiliar)
    */
    public double getDistributorSalesById(int year, Integer distributorID){
        Cliente c = this.CById(distributorID);
        double sum = 0;
        //  Para cada distribuidor (cliente) recorremos sus ordenes, y por cada
        //  orden recorremos las ventas filtradas por año
        for(Orden o : c.getOrdenCollection()){
            for(Venta v : o.getVentaCollection()){
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getFecCobro());
                if (cal.get(Calendar.YEAR) == year) {
                    double total = v.getCantidad() * v.getMonto();
                    sum += total;
                }
            }
        }
        return sum;
    }
    
    /*
        Comparar distribuidores
    */
    public double compareDistributorSales(int year, Integer dist1, Integer dist2){
        //  Calculamos las ventas totales de cada uno y obtenemos su diferencia
        double c1sum = getDistributorSalesById(year, dist1);
        double c2sum = getDistributorSalesById(year, dist2);
        return c1sum - c2sum;
    }

    /*
        Calcula pendiente por cobrar total
    */
    public double getPendienteCobrarAnual(int year) {
        double orderSum = 0;
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                orderSum += o.getTotalPago();
                //  Ahora vemos los pagos que han sido realizados a esta orden
                //  y los descontamos al total
                for(Venta v : o.getVentaCollection()){
                    orderSum -= v.getCantidad() * v.getMonto();
                }
            }
        }
        return orderSum;
    }
    
    /*
        Calcula pendiente por cobrar total por distribuidor (auxiliar)
    */
    public NumericTableSet getPendienteCobrarDistribuidorTable(int year) {
        NumericTableSet acumOrder = new NumericTableSet();
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                Cliente cliente = o.getClienteId();
                String distribuidorID = cliente.getId().toString();
                String distributorName = cliente.getNombre() + " " + cliente.getAppaterno() + " " + cliente.getApmaterno();
                String key = distribuidorID + " " + distributorName;
                double total = o.getTotalPago();
                //  Ahora a esta orden, le vamos a descontar todos los pagos realizados
                for(Venta v : o.getVentaCollection()){
                    total -= v.getCantidad() * v.getMonto();
                }
                acumOrder.addValue(key, total);
            }
        }
        //  Realiza la sumatoria de los totales de las ordenesde cada uno de los distribuidores
        return acumOrder;
    }
    
    /*
        Calcula pendiente por cobrar total por distribuidor
    */
   public List<Record> getPendienteCobrarDistribuidor(int year) {
        NumericTreeRow table = this.getPendienteCobrarDistribuidorTable(year).getSumRow();
        List<Record> records = new ArrayList<>();
        for(Column c : table.getDescendingColumns()){
            //  Separamos el id del nombre del cliente
            String[] ls = c.name.split(" ");
            int lsn = ls[0].length();
            Record rcd = new Record(Integer.parseInt(ls[0]), 
                                    c.name.substring(lsn + 1),
                                    c.value);
            records.add(rcd);
        }
        return records;
    }
    
    /*
        Pendiente por producir total
    */
    public double getPendienteProducirYear(int year) {
        double orderSum = 0;
        for (Orden o : this.O()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(o.getFecAlta());
            if (cal.get(Calendar.YEAR) == year) {
                Estatus estatus = o.getEstatusId();
                //  5 = producido
                if(estatus.getId() < 5){
                    orderSum = orderSum + o.getTotalPago();
                }
            }
        }
        return orderSum;
    }
}
