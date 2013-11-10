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
public class CompraComponentePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "compra_id")
    private int compraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "componente_id")
    private int componenteId;

    public CompraComponentePK() {
    }

    public CompraComponentePK(int compraId, int componenteId) {
        this.compraId = compraId;
        this.componenteId = componenteId;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
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
        hash += (int) compraId;
        hash += (int) componenteId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompraComponentePK)) {
            return false;
        }
        CompraComponentePK other = (CompraComponentePK) object;
        if (this.compraId != other.compraId) {
            return false;
        }
        if (this.componenteId != other.componenteId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CompraComponentePK[ compraId=" + compraId + ", componenteId=" + componenteId + " ]";
    }
    
}
