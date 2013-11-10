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
@Table(name = "orden_modelo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenModelo.findAll", query = "SELECT o FROM OrdenModelo o"),
    @NamedQuery(name = "OrdenModelo.findByOrdenId", query = "SELECT o FROM OrdenModelo o WHERE o.ordenModeloPK.ordenId = :ordenId"),
    @NamedQuery(name = "OrdenModelo.findByModeloId", query = "SELECT o FROM OrdenModelo o WHERE o.ordenModeloPK.modeloId = :modeloId"),
    @NamedQuery(name = "OrdenModelo.findByCantidad", query = "SELECT o FROM OrdenModelo o WHERE o.cantidad = :cantidad"),
    @NamedQuery(name = "OrdenModelo.findByFabricadas", query = "SELECT o FROM OrdenModelo o WHERE o.fabricadas = :fabricadas")})
public class OrdenModelo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrdenModeloPK ordenModeloPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fabricadas")
    private int fabricadas;
    @JoinColumn(name = "orden_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orden orden;
    @JoinColumn(name = "modelo_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Modelo modelo;

    public OrdenModelo() {
    }

    public OrdenModelo(OrdenModeloPK ordenModeloPK) {
        this.ordenModeloPK = ordenModeloPK;
    }

    public OrdenModelo(OrdenModeloPK ordenModeloPK, int cantidad, int fabricadas) {
        this.ordenModeloPK = ordenModeloPK;
        this.cantidad = cantidad;
        this.fabricadas = fabricadas;
    }

    public OrdenModelo(int ordenId, int modeloId) {
        this.ordenModeloPK = new OrdenModeloPK(ordenId, modeloId);
    }

    public OrdenModeloPK getOrdenModeloPK() {
        return ordenModeloPK;
    }

    public void setOrdenModeloPK(OrdenModeloPK ordenModeloPK) {
        this.ordenModeloPK = ordenModeloPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getFabricadas() {
        return fabricadas;
    }

    public void setFabricadas(int fabricadas) {
        this.fabricadas = fabricadas;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordenModeloPK != null ? ordenModeloPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenModelo)) {
            return false;
        }
        OrdenModelo other = (OrdenModelo) object;
        if ((this.ordenModeloPK == null && other.ordenModeloPK != null) || (this.ordenModeloPK != null && !this.ordenModeloPK.equals(other.ordenModeloPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.OrdenModelo[ ordenModeloPK=" + ordenModeloPK + " ]";
    }
    
}
