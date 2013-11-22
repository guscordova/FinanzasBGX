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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "orden")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orden.findAll", query = "SELECT o FROM Orden o"),
    @NamedQuery(name = "Orden.findPendientesProducir", query = "SELECT o FROM Orden o INNER JOIN o.estatusId e WHERE e.id < 5"),
    @NamedQuery(name = "Orden.findById", query = "SELECT o FROM Orden o WHERE o.id = :id"),
    @NamedQuery(name = "Orden.findByFecAlta", query = "SELECT o FROM Orden o WHERE o.fecAlta = :fecAlta"),
    @NamedQuery(name = "Orden.findByFecEntrega", query = "SELECT o FROM Orden o WHERE o.fecEntrega = :fecEntrega"),
    @NamedQuery(name = "Orden.findByTotalPago", query = "SELECT o FROM Orden o WHERE o.totalPago = :totalPago"),
    @NamedQuery(name = "Orden.findByComentarios", query = "SELECT o FROM Orden o WHERE o.comentarios = :comentarios")})
public class Orden implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fec_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAlta;
    @Column(name = "fec_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEntrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_pago")
    private double totalPago;
    @Size(max = 255)
    @Column(name = "comentarios")
    private String comentarios;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordenId")
    private Collection<Venta> ventaCollection;
    @JoinColumn(name = "estatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estatus estatusId;
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente clienteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orden")
    private Collection<OrdenModelo> ordenModeloCollection;

    public Orden() {
    }

    public Orden(Integer id) {
        this.id = id;
    }

    public Orden(Integer id, Date fecAlta, double totalPago) {
        this.id = id;
        this.fecAlta = fecAlta;
        this.totalPago = totalPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecAlta() {
        return fecAlta;
    }

    public void setFecAlta(Date fecAlta) {
        this.fecAlta = fecAlta;
    }

    public Date getFecEntrega() {
        return fecEntrega;
    }

    public void setFecEntrega(Date fecEntrega) {
        this.fecEntrega = fecEntrega;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @XmlTransient
    public Collection<Venta> getVentaCollection() {
        return ventaCollection;
    }

    public void setVentaCollection(Collection<Venta> ventaCollection) {
        this.ventaCollection = ventaCollection;
    }

    public Estatus getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(Estatus estatusId) {
        this.estatusId = estatusId;
    }

    public Cliente getClienteId() {
        return clienteId;
    }

    public void setClienteId(Cliente clienteId) {
        this.clienteId = clienteId;
    }

    @XmlTransient
    public Collection<OrdenModelo> getOrdenModeloCollection() {
        return ordenModeloCollection;
    }

    public void setOrdenModeloCollection(Collection<OrdenModelo> ordenModeloCollection) {
        this.ordenModeloCollection = ordenModeloCollection;
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
        if (!(object instanceof Orden)) {
            return false;
        }
        Orden other = (Orden) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Orden[ id=" + id + " ]";
    }
    
}
