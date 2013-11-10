/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findById", query = "SELECT v FROM Venta v WHERE v.id = :id"),
    @NamedQuery(name = "Venta.findByCantidad", query = "SELECT v FROM Venta v WHERE v.cantidad = :cantidad"),
    @NamedQuery(name = "Venta.findByFecEntrega", query = "SELECT v FROM Venta v WHERE v.fecEntrega = :fecEntrega"),
    @NamedQuery(name = "Venta.findByFecCobro", query = "SELECT v FROM Venta v WHERE v.fecCobro = :fecCobro"),
    @NamedQuery(name = "Venta.findByMonto", query = "SELECT v FROM Venta v WHERE v.monto = :monto"),
    @NamedQuery(name = "Venta.findByEstatus", query = "SELECT v FROM Venta v WHERE v.estatus = :estatus")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "fec_entrega")
    @Temporal(TemporalType.DATE)
    private Date fecEntrega;
    @Column(name = "fec_cobro")
    @Temporal(TemporalType.DATE)
    private Date fecCobro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private Double monto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private short estatus;
    @JoinColumn(name = "orden_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Orden ordenId;

    public Venta() {
    }

    public Venta(Integer id) {
        this.id = id;
    }

    public Venta(Integer id, int cantidad, short estatus) {
        this.id = id;
        this.cantidad = cantidad;
        this.estatus = estatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecEntrega() {
        return fecEntrega;
    }

    public void setFecEntrega(Date fecEntrega) {
        this.fecEntrega = fecEntrega;
    }

    public Date getFecCobro() {
        return fecCobro;
    }

    public void setFecCobro(Date fecCobro) {
        this.fecCobro = fecCobro;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public short getEstatus() {
        return estatus;
    }

    public void setEstatus(short estatus) {
        this.estatus = estatus;
    }

    public Orden getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Orden ordenId) {
        this.ordenId = ordenId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Venta[ id=" + id + " ]";
    }
    
}
