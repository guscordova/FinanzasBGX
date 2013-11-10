/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competencia.findAll", query = "SELECT c FROM Competencia c"),
    @NamedQuery(name = "Competencia.findById", query = "SELECT c FROM Competencia c WHERE c.id = :id"),
    @NamedQuery(name = "Competencia.findByNombre", query = "SELECT c FROM Competencia c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Competencia.findByDescripcion", query = "SELECT c FROM Competencia c WHERE c.descripcion = :descripcion")})
public class Competencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinTable(name = "evaluacion_competencia", joinColumns = {
        @JoinColumn(name = "competencia_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Evaluacion> evaluacionCollection;
    @JoinTable(name = "curso_competencia", joinColumns = {
        @JoinColumn(name = "competencia_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "curso_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Curso> cursoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competencia")
    private Collection<PerfilCompetencia> perfilCompetenciaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competencia")
    private Collection<EmpleadoCompetencia> empleadoCompetenciaCollection;

    public Competencia() {
    }

    public Competencia(Integer id) {
        this.id = id;
    }

    public Competencia(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Evaluacion> getEvaluacionCollection() {
        return evaluacionCollection;
    }

    public void setEvaluacionCollection(Collection<Evaluacion> evaluacionCollection) {
        this.evaluacionCollection = evaluacionCollection;
    }

    @XmlTransient
    public Collection<Curso> getCursoCollection() {
        return cursoCollection;
    }

    public void setCursoCollection(Collection<Curso> cursoCollection) {
        this.cursoCollection = cursoCollection;
    }

    @XmlTransient
    public Collection<PerfilCompetencia> getPerfilCompetenciaCollection() {
        return perfilCompetenciaCollection;
    }

    public void setPerfilCompetenciaCollection(Collection<PerfilCompetencia> perfilCompetenciaCollection) {
        this.perfilCompetenciaCollection = perfilCompetenciaCollection;
    }

    @XmlTransient
    public Collection<EmpleadoCompetencia> getEmpleadoCompetenciaCollection() {
        return empleadoCompetenciaCollection;
    }

    public void setEmpleadoCompetenciaCollection(Collection<EmpleadoCompetencia> empleadoCompetenciaCollection) {
        this.empleadoCompetenciaCollection = empleadoCompetenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Competencia)) {
            return false;
        }
        Competencia other = (Competencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Competencia[ id=" + id + " ]";
    }
    
}
