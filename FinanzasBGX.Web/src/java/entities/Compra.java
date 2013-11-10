/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findById", query = "SELECT c FROM Compra c WHERE c.id = :id"),
    @NamedQuery(name = "Compra.findByEstatus", query = "SELECT c FROM Compra c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "Compra.findByFecColocado", query = "SELECT c FROM Compra c WHERE c.fecColocado = :fecColocado"),
    @NamedQuery(name = "Compra.findByFecSurtido", query = "SELECT c FROM Compra c WHERE c.fecSurtido = :fecSurtido"),
    @NamedQuery(name = "Compra.findByFecPago", query = "SELECT c FROM Compra c WHERE c.fecPago = :fecPago"),
    @NamedQuery(name = "Compra.findByCostoTotal", query = "SELECT c FROM Compra c WHERE c.costoTotal = :costoTotal")})
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private short estatus;
    @Column(name = "fec_colocado")
    @Temporal(TemporalType.DATE)
    private Date fecColocado;
    @Column(name = "fec_surtido")
    @Temporal(TemporalType.DATE)
    private Date fecSurtido;
    @Column(name = "fec_pago")
    @Temporal(TemporalType.DATE)
    private Date fecPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_total")
    private double costoTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compra")
    private Collection<CompraComponente> compraComponenteCollection;
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedor proveedorId;

    public Compra() {
    }

    public Compra(Integer id) {
        this.id = id;
    }

    public Compra(Integer id, short estatus, double costoTotal) {
        this.id = id;
        this.estatus = estatus;
        this.costoTotal = costoTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getEstatus() {
        return estatus;
    }

    public void setEstatus(short estatus) {
        this.estatus = estatus;
    }

    public Date getFecColocado() {
        return fecColocado;
    }

    public void setFecColocado(Date fecColocado) {
        this.fecColocado = fecColocado;
    }

    public Date getFecSurtido() {
        return fecSurtido;
    }

    public void setFecSurtido(Date fecSurtido) {
        this.fecSurtido = fecSurtido;
    }

    public Date getFecPago() {
        return fecPago;
    }

    public void setFecPago(Date fecPago) {
        this.fecPago = fecPago;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    @XmlTransient
    public Collection<CompraComponente> getCompraComponenteCollection() {
        return compraComponenteCollection;
    }

    public void setCompraComponenteCollection(Collection<CompraComponente> compraComponenteCollection) {
        this.compraComponenteCollection = compraComponenteCollection;
    }

    public Proveedor getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Proveedor proveedorId) {
        this.proveedorId = proveedorId;
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Compra[ id=" + id + " ]";
    }
    
}
