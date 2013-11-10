/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "pedido_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidoComponente.findAll", query = "SELECT p FROM PedidoComponente p"),
    @NamedQuery(name = "PedidoComponente.findByPedidoId", query = "SELECT p FROM PedidoComponente p WHERE p.pedidoComponentePK.pedidoId = :pedidoId"),
    @NamedQuery(name = "PedidoComponente.findByComponenteId", query = "SELECT p FROM PedidoComponente p WHERE p.pedidoComponentePK.componenteId = :componenteId"),
    @NamedQuery(name = "PedidoComponente.findByCantidad", query = "SELECT p FROM PedidoComponente p WHERE p.cantidad = :cantidad")})
public class PedidoComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidoComponentePK pedidoComponentePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "pedido_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "componente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente componente;

    public PedidoComponente() {
    }

    public PedidoComponente(PedidoComponentePK pedidoComponentePK) {
        this.pedidoComponentePK = pedidoComponentePK;
    }

    public PedidoComponente(PedidoComponentePK pedidoComponentePK, int cantidad) {
        this.pedidoComponentePK = pedidoComponentePK;
        this.cantidad = cantidad;
    }

    public PedidoComponente(int pedidoId, int componenteId) {
        this.pedidoComponentePK = new PedidoComponentePK(pedidoId, componenteId);
    }

    public PedidoComponentePK getPedidoComponentePK() {
        return pedidoComponentePK;
    }

    public void setPedidoComponentePK(PedidoComponentePK pedidoComponentePK) {
        this.pedidoComponentePK = pedidoComponentePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoComponentePK != null ? pedidoComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoComponente)) {
            return false;
        }
        PedidoComponente other = (PedidoComponente) object;
        if ((this.pedidoComponentePK == null && other.pedidoComponentePK != null) || (this.pedidoComponentePK != null && !this.pedidoComponentePK.equals(other.pedidoComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PedidoComponente[ pedidoComponentePK=" + pedidoComponentePK + " ]";
    }
    
}
