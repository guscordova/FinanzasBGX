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
public class PerfilCompetenciaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil_id")
    private int perfilId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "competencia_id")
    private int competenciaId;

    public PerfilCompetenciaPK() {
    }

    public PerfilCompetenciaPK(int perfilId, int competenciaId) {
        this.perfilId = perfilId;
        this.competenciaId = competenciaId;
    }

    public int getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(int perfilId) {
        this.perfilId = perfilId;
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
        hash += (int) perfilId;
        hash += (int) competenciaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilCompetenciaPK)) {
            return false;
        }
        PerfilCompetenciaPK other = (PerfilCompetenciaPK) object;
        if (this.perfilId != other.perfilId) {
            return false;
        }
        if (this.competenciaId != other.competenciaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PerfilCompetenciaPK[ perfilId=" + perfilId + ", competenciaId=" + competenciaId + " ]";
    }
    
}
