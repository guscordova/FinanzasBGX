/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robertogarza
 */
@Entity
@Table(name = "modelo_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModeloComponente.findAll", query = "SELECT m FROM ModeloComponente m"),
    @NamedQuery(name = "ModeloComponente.findByModeloId", query = "SELECT m FROM ModeloComponente m WHERE m.modeloComponentePK.modeloId = :modeloId"),
    @NamedQuery(name = "ModeloComponente.findByComponenteId", query = "SELECT m FROM ModeloComponente m WHERE m.modeloComponentePK.componenteId = :componenteId"),
    @NamedQuery(name = "ModeloComponente.findByCantidad", query = "SELECT m FROM ModeloComponente m WHERE m.cantidad = :cantidad")})
public class ModeloComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ModeloComponentePK modeloComponentePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "modelo_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Modelo modelo;
    @JoinColumn(name = "componente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente componente;

    public ModeloComponente() {
    }

    public ModeloComponente(ModeloComponentePK modeloComponentePK) {
        this.modeloComponentePK = modeloComponentePK;
    }

    public ModeloComponente(ModeloComponentePK modeloComponentePK, int cantidad) {
        this.modeloComponentePK = modeloComponentePK;
        this.cantidad = cantidad;
    }

    public ModeloComponente(int modeloId, int componenteId) {
        this.modeloComponentePK = new ModeloComponentePK(modeloId, componenteId);
    }

    public ModeloComponentePK getModeloComponentePK() {
        return modeloComponentePK;
    }

    public void setModeloComponentePK(ModeloComponentePK modeloComponentePK) {
        this.modeloComponentePK = modeloComponentePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modeloComponentePK != null ? modeloComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeloComponente)) {
            return false;
        }
        ModeloComponente other = (ModeloComponente) object;
        if ((this.modeloComponentePK == null && other.modeloComponentePK != null) || (this.modeloComponentePK != null && !this.modeloComponentePK.equals(other.modeloComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ModeloComponente[ modeloComponentePK=" + modeloComponentePK + " ]";
    }
    
}
