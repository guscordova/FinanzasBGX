/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "curso_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursoEmpleado.findAll", query = "SELECT c FROM CursoEmpleado c"),
    @NamedQuery(name = "CursoEmpleado.findByCursoId", query = "SELECT c FROM CursoEmpleado c WHERE c.cursoEmpleadoPK.cursoId = :cursoId"),
    @NamedQuery(name = "CursoEmpleado.findByEmpleadoId", query = "SELECT c FROM CursoEmpleado c WHERE c.cursoEmpleadoPK.empleadoId = :empleadoId"),
    @NamedQuery(name = "CursoEmpleado.findByFecha", query = "SELECT c FROM CursoEmpleado c WHERE c.fecha = :fecha")})
public class CursoEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CursoEmpleadoPK cursoEmpleadoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "empleado_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empleado empleado;
    @JoinColumn(name = "curso_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Curso curso;

    public CursoEmpleado() {
    }

    public CursoEmpleado(CursoEmpleadoPK cursoEmpleadoPK) {
        this.cursoEmpleadoPK = cursoEmpleadoPK;
    }

    public CursoEmpleado(CursoEmpleadoPK cursoEmpleadoPK, Date fecha) {
        this.cursoEmpleadoPK = cursoEmpleadoPK;
        this.fecha = fecha;
    }

    public CursoEmpleado(int cursoId, int empleadoId) {
        this.cursoEmpleadoPK = new CursoEmpleadoPK(cursoId, empleadoId);
    }

    public CursoEmpleadoPK getCursoEmpleadoPK() {
        return cursoEmpleadoPK;
    }

    public void setCursoEmpleadoPK(CursoEmpleadoPK cursoEmpleadoPK) {
        this.cursoEmpleadoPK = cursoEmpleadoPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cursoEmpleadoPK != null ? cursoEmpleadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursoEmpleado)) {
            return false;
        }
        CursoEmpleado other = (CursoEmpleado) object;
        if ((this.cursoEmpleadoPK == null && other.cursoEmpleadoPK != null) || (this.cursoEmpleadoPK != null && !this.cursoEmpleadoPK.equals(other.cursoEmpleadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CursoEmpleado[ cursoEmpleadoPK=" + cursoEmpleadoPK + " ]";
    }
    
}
