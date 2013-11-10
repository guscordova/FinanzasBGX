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
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findById", query = "SELECT p FROM Pedido p WHERE p.id = :id"),
    @NamedQuery(name = "Pedido.findByEstatus", query = "SELECT p FROM Pedido p WHERE p.estatus = :estatus"),
    @NamedQuery(name = "Pedido.findByFecColocado", query = "SELECT p FROM Pedido p WHERE p.fecColocado = :fecColocado"),
    @NamedQuery(name = "Pedido.findByFecSurtido", query = "SELECT p FROM Pedido p WHERE p.fecSurtido = :fecSurtido")})
public class Pedido implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<PedidoComponente> pedidoComponenteCollection;

    public Pedido() {
    }

    public Pedido(Integer id) {
        this.id = id;
    }

    public Pedido(Integer id, short estatus) {
        this.id = id;
        this.estatus = estatus;
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

    @XmlTransient
    public Collection<PedidoComponente> getPedidoComponenteCollection() {
        return pedidoComponenteCollection;
    }

    public void setPedidoComponenteCollection(Collection<PedidoComponente> pedidoComponenteCollection) {
        this.pedidoComponenteCollection = pedidoComponenteCollection;
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
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pedido[ id=" + id + " ]";
    }
    
}
