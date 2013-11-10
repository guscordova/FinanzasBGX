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
public class OrdenModeloPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden_id")
    private int ordenId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modelo_id")
    private int modeloId;

    public OrdenModeloPK() {
    }

    public OrdenModeloPK(int ordenId, int modeloId) {
        this.ordenId = ordenId;
        this.modeloId = modeloId;
    }

    public int getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(int ordenId) {
        this.ordenId = ordenId;
    }

    public int getModeloId() {
        return modeloId;
    }

    public void setModeloId(int modeloId) {
        this.modeloId = modeloId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ordenId;
        hash += (int) modeloId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenModeloPK)) {
            return false;
        }
        OrdenModeloPK other = (OrdenModeloPK) object;
        if (this.ordenId != other.ordenId) {
            return false;
        }
        if (this.modeloId != other.modeloId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.OrdenModeloPK[ ordenId=" + ordenId + ", modeloId=" + modeloId + " ]";
    }
    
}
