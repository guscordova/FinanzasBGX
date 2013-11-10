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
@Table(name = "proveedor_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProveedorComponente.findAll", query = "SELECT p FROM ProveedorComponente p"),
    @NamedQuery(name = "ProveedorComponente.findByProveedorId", query = "SELECT p FROM ProveedorComponente p WHERE p.proveedorComponentePK.proveedorId = :proveedorId"),
    @NamedQuery(name = "ProveedorComponente.findByComponenteId", query = "SELECT p FROM ProveedorComponente p WHERE p.proveedorComponentePK.componenteId = :componenteId"),
    @NamedQuery(name = "ProveedorComponente.findByCosto", query = "SELECT p FROM ProveedorComponente p WHERE p.costo = :costo")})
public class ProveedorComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProveedorComponentePK proveedorComponentePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private double costo;
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumn(name = "componente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente componente;

    public ProveedorComponente() {
    }

    public ProveedorComponente(ProveedorComponentePK proveedorComponentePK) {
        this.proveedorComponentePK = proveedorComponentePK;
    }

    public ProveedorComponente(ProveedorComponentePK proveedorComponentePK, double costo) {
        this.proveedorComponentePK = proveedorComponentePK;
        this.costo = costo;
    }

    public ProveedorComponente(int proveedorId, int componenteId) {
        this.proveedorComponentePK = new ProveedorComponentePK(proveedorId, componenteId);
    }

    public ProveedorComponentePK getProveedorComponentePK() {
        return proveedorComponentePK;
    }

    public void setProveedorComponentePK(ProveedorComponentePK proveedorComponentePK) {
        this.proveedorComponentePK = proveedorComponentePK;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
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
        hash += (proveedorComponentePK != null ? proveedorComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProveedorComponente)) {
            return false;
        }
        ProveedorComponente other = (ProveedorComponente) object;
        if ((this.proveedorComponentePK == null && other.proveedorComponentePK != null) || (this.proveedorComponentePK != null && !this.proveedorComponentePK.equals(other.proveedorComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProveedorComponente[ proveedorComponentePK=" + proveedorComponentePK + " ]";
    }
    
}
