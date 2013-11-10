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
@Table(name = "perfil_competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PerfilCompetencia.findAll", query = "SELECT p FROM PerfilCompetencia p"),
    @NamedQuery(name = "PerfilCompetencia.findByPerfilId", query = "SELECT p FROM PerfilCompetencia p WHERE p.perfilCompetenciaPK.perfilId = :perfilId"),
    @NamedQuery(name = "PerfilCompetencia.findByCompetenciaId", query = "SELECT p FROM PerfilCompetencia p WHERE p.perfilCompetenciaPK.competenciaId = :competenciaId"),
    @NamedQuery(name = "PerfilCompetencia.findByComentarios", query = "SELECT p FROM PerfilCompetencia p WHERE p.comentarios = :comentarios")})
public class PerfilCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PerfilCompetenciaPK perfilCompetenciaPK;
    @Size(max = 500)
    @Column(name = "comentarios")
    private String comentarios;
    @JoinColumn(name = "perfil_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Perfil perfil;
    @JoinColumn(name = "competencia_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Competencia competencia;

    public PerfilCompetencia() {
    }

    public PerfilCompetencia(PerfilCompetenciaPK perfilCompetenciaPK) {
        this.perfilCompetenciaPK = perfilCompetenciaPK;
    }

    public PerfilCompetencia(int perfilId, int competenciaId) {
        this.perfilCompetenciaPK = new PerfilCompetenciaPK(perfilId, competenciaId);
    }

    public PerfilCompetenciaPK getPerfilCompetenciaPK() {
        return perfilCompetenciaPK;
    }

    public void setPerfilCompetenciaPK(PerfilCompetenciaPK perfilCompetenciaPK) {
        this.perfilCompetenciaPK = perfilCompetenciaPK;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
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
        hash += (perfilCompetenciaPK != null ? perfilCompetenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilCompetencia)) {
            return false;
        }
        PerfilCompetencia other = (PerfilCompetencia) object;
        if ((this.perfilCompetenciaPK == null && other.perfilCompetenciaPK != null) || (this.perfilCompetenciaPK != null && !this.perfilCompetenciaPK.equals(other.perfilCompetenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PerfilCompetencia[ perfilCompetenciaPK=" + perfilCompetenciaPK + " ]";
    }
    
}
