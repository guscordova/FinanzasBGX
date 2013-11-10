/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "empleado_competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadoCompetencia.findAll", query = "SELECT e FROM EmpleadoCompetencia e"),
    @NamedQuery(name = "EmpleadoCompetencia.findByEmpleadoId", query = "SELECT e FROM EmpleadoCompetencia e WHERE e.empleadoCompetenciaPK.empleadoId = :empleadoId"),
    @NamedQuery(name = "EmpleadoCompetencia.findByCompetenciaId", query = "SELECT e FROM EmpleadoCompetencia e WHERE e.empleadoCompetenciaPK.competenciaId = :competenciaId"),
    @NamedQuery(name = "EmpleadoCompetencia.findByComentarios", query = "SELECT e FROM EmpleadoCompetencia e WHERE e.comentarios = :comentarios")})
public class EmpleadoCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmpleadoCompetenciaPK empleadoCompetenciaPK;
    @Size(max = 500)
    @Column(name = "comentarios")
    private String comentarios;
    @JoinColumn(name = "empleado_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empleado empleado;
    @JoinColumn(name = "competencia_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Competencia competencia;

    public EmpleadoCompetencia() {
    }

    public EmpleadoCompetencia(EmpleadoCompetenciaPK empleadoCompetenciaPK) {
        this.empleadoCompetenciaPK = empleadoCompetenciaPK;
    }

    public EmpleadoCompetencia(int empleadoId, int competenciaId) {
        this.empleadoCompetenciaPK = new EmpleadoCompetenciaPK(empleadoId, competenciaId);
    }

    public EmpleadoCompetenciaPK getEmpleadoCompetenciaPK() {
        return empleadoCompetenciaPK;
    }

    public void setEmpleadoCompetenciaPK(EmpleadoCompetenciaPK empleadoCompetenciaPK) {
        this.empleadoCompetenciaPK = empleadoCompetenciaPK;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadoCompetenciaPK != null ? empleadoCompetenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoCompetencia)) {
            return false;
        }
        EmpleadoCompetencia other = (EmpleadoCompetencia) object;
        if ((this.empleadoCompetenciaPK == null && other.empleadoCompetenciaPK != null) || (this.empleadoCompetenciaPK != null && !this.empleadoCompetenciaPK.equals(other.empleadoCompetenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EmpleadoCompetencia[ empleadoCompetenciaPK=" + empleadoCompetenciaPK + " ]";
    }
    
}
