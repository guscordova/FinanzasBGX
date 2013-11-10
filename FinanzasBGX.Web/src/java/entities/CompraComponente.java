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
@Table(name = "compra_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompraComponente.findAll", query = "SELECT c FROM CompraComponente c"),
    @NamedQuery(name = "CompraComponente.findByCompraId", query = "SELECT c FROM CompraComponente c WHERE c.compraComponentePK.compraId = :compraId"),
    @NamedQuery(name = "CompraComponente.findByComponenteId", query = "SELECT c FROM CompraComponente c WHERE c.compraComponentePK.componenteId = :componenteId"),
    @NamedQuery(name = "CompraComponente.findByCantidad", query = "SELECT c FROM CompraComponente c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "CompraComponente.findByCosto", query = "SELECT c FROM CompraComponente c WHERE c.costo = :costo")})
public class CompraComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompraComponentePK compraComponentePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private double costo;
    @JoinColumn(name = "compra_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Compra compra;
    @JoinColumn(name = "componente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente componente;

    public CompraComponente() {
    }

    public CompraComponente(CompraComponentePK compraComponentePK) {
        this.compraComponentePK = compraComponentePK;
    }

    public CompraComponente(CompraComponentePK compraComponentePK, int cantidad, double costo) {
        this.compraComponentePK = compraComponentePK;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public CompraComponente(int compraId, int componenteId) {
        this.compraComponentePK = new CompraComponentePK(compraId, componenteId);
    }

    public CompraComponentePK getCompraComponentePK() {
        return compraComponentePK;
    }

    public void setCompraComponentePK(CompraComponentePK compraComponentePK) {
        this.compraComponentePK = compraComponentePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
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
        hash += (compraComponentePK != null ? compraComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompraComponente)) {
            return false;
        }
        CompraComponente other = (CompraComponente) object;
        if ((this.compraComponentePK == null && other.compraComponentePK != null) || (this.compraComponentePK != null && !this.compraComponentePK.equals(other.compraComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CompraComponente[ compraComponentePK=" + compraComponentePK + " ]";
    }
    
}
