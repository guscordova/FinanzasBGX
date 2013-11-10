/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author robertogarza
 */
@Embeddable
public class PedidoComponentePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "pedido_id")
    private int pedidoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "componente_id")
    private int componenteId;

    public PedidoComponentePK() {
    }

    public PedidoComponentePK(int pedidoId, int componenteId) {
        this.pedidoId = pedidoId;
        this.componenteId = componenteId;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(int componenteId) {
        this.componenteId = componenteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pedidoId;
        hash += (int) componenteId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoComponentePK)) {
            return false;
        }
        PedidoComponentePK other = (PedidoComponentePK) object;
        if (this.pedidoId != other.pedidoId) {
            return false;
        }
        if (this.componenteId != other.componenteId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PedidoComponentePK[ pedidoId=" + pedidoId + ", componenteId=" + componenteId + " ]";
    }
    
}
