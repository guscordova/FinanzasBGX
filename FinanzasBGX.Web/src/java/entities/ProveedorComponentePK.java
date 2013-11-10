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
public class ProveedorComponentePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "proveedor_id")
    private int proveedorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "componente_id")
    private int componenteId;

    public ProveedorComponentePK() {
    }

    public ProveedorComponentePK(int proveedorId, int componenteId) {
        this.proveedorId = proveedorId;
        this.componenteId = componenteId;
    }

    public int getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
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
        hash += (int) proveedorId;
        hash += (int) componenteId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProveedorComponentePK)) {
            return false;
        }
        ProveedorComponentePK other = (ProveedorComponentePK) object;
        if (this.proveedorId != other.proveedorId) {
            return false;
        }
        if (this.componenteId != other.componenteId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProveedorComponentePK[ proveedorId=" + proveedorId + ", componenteId=" + componenteId + " ]";
    }
    
}
