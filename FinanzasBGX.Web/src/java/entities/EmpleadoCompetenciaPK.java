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
public class EmpleadoCompetenciaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "empleado_id")
    private int empleadoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "competencia_id")
    private int competenciaId;

    public EmpleadoCompetenciaPK() {
    }

    public EmpleadoCompetenciaPK(int empleadoId, int competenciaId) {
        this.empleadoId = empleadoId;
        this.competenciaId = competenciaId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public int getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(int competenciaId) {
        this.competenciaId = competenciaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) empleadoId;
        hash += (int) competenciaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoCompetenciaPK)) {
            return false;
        }
        EmpleadoCompetenciaPK other = (EmpleadoCompetenciaPK) object;
        if (this.empleadoId != other.empleadoId) {
            return false;
        }
        if (this.competenciaId != other.competenciaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EmpleadoCompetenciaPK[ empleadoId=" + empleadoId + ", competenciaId=" + competenciaId + " ]";
    }
    
}
