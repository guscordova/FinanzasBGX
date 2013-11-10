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
public class CursoEmpleadoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "curso_id")
    private int cursoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empleado_id")
    private int empleadoId;

    public CursoEmpleadoPK() {
    }

    public CursoEmpleadoPK(int cursoId, int empleadoId) {
        this.cursoId = cursoId;
        this.empleadoId = empleadoId;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        this.cursoId = cursoId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cursoId;
        hash += (int) empleadoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursoEmpleadoPK)) {
            return false;
        }
        CursoEmpleadoPK other = (CursoEmpleadoPK) object;
        if (this.cursoId != other.cursoId) {
            return false;
        }
        if (this.empleadoId != other.empleadoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CursoEmpleadoPK[ cursoId=" + cursoId + ", empleadoId=" + empleadoId + " ]";
    }
    
}
