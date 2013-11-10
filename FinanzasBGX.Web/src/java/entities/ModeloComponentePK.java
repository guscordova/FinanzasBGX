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
public class ModeloComponentePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "modelo_id")
    private int modeloId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "componente_id")
    private int componenteId;

    public ModeloComponentePK() {
    }

    public ModeloComponentePK(int modeloId, int componenteId) {
        this.modeloId = modeloId;
        this.componenteId = componenteId;
    }

    public int getModeloId() {
        return modeloId;
    }

    public void setModeloId(int modeloId) {
        this.modeloId = modeloId;
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
        hash += (int) modeloId;
        hash += (int) componenteId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeloComponentePK)) {
            return false;
        }
        ModeloComponentePK other = (ModeloComponentePK) object;
        if (this.modeloId != other.modeloId) {
            return false;
        }
        if (this.componenteId != other.componenteId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ModeloComponentePK[ modeloId=" + modeloId + ", componenteId=" + componenteId + " ]";
    }
    
}
