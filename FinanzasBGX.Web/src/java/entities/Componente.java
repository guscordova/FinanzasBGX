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
import javax.persistence.ManyToOne;
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
@Table(name = "componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Componente.findAll", query = "SELECT c FROM Componente c"),
    @NamedQuery(name = "Componente.findById", query = "SELECT c FROM Componente c WHERE c.id = :id"),
    @NamedQuery(name = "Componente.findByNombre", query = "SELECT c FROM Componente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Componente.findByDescripcion", query = "SELECT c FROM Componente c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Componente.findByCodigo", query = "SELECT c FROM Componente c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Componente.findByExistencia", query = "SELECT c FROM Componente c WHERE c.existencia = :existencia")})
public class Componente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "existencia")
    private int existencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
    private Collection<CompraComponente> compraComponenteCollection;
    @JoinColumn(name = "tipo_componente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoComponente tipoComponenteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
    private Collection<PedidoComponente> pedidoComponenteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
    private Collection<ProveedorComponente> proveedorComponenteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
    private Collection<ModeloComponente> modeloComponenteCollection;

    public Componente() {
    }

    public Componente(Integer id) {
        this.id = id;
    }

    public Componente(Integer id, String nombre, int existencia) {
        this.id = id;
        this.nombre = nombre;
        this.existencia = existencia;
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    @XmlTransient
    public Collection<CompraComponente> getCompraComponenteCollection() {
        return compraComponenteCollection;
    }

    public void setCompraComponenteCollection(Collection<CompraComponente> compraComponenteCollection) {
        this.compraComponenteCollection = compraComponenteCollection;
    }

    public TipoComponente getTipoComponenteId() {
        return tipoComponenteId;
    }

    public void setTipoComponenteId(TipoComponente tipoComponenteId) {
        this.tipoComponenteId = tipoComponenteId;
    }

    @XmlTransient
    public Collection<PedidoComponente> getPedidoComponenteCollection() {
        return pedidoComponenteCollection;
    }

    public void setPedidoComponenteCollection(Collection<PedidoComponente> pedidoComponenteCollection) {
        this.pedidoComponenteCollection = pedidoComponenteCollection;
    }

    @XmlTransient
    public Collection<ProveedorComponente> getProveedorComponenteCollection() {
        return proveedorComponenteCollection;
    }

    public void setProveedorComponenteCollection(Collection<ProveedorComponente> proveedorComponenteCollection) {
        this.proveedorComponenteCollection = proveedorComponenteCollection;
    }

    @XmlTransient
    public Collection<ModeloComponente> getModeloComponenteCollection() {
        return modeloComponenteCollection;
    }

    public void setModeloComponenteCollection(Collection<ModeloComponente> modeloComponenteCollection) {
        this.modeloComponenteCollection = modeloComponenteCollection;
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
        if (!(object instanceof Componente)) {
            return false;
        }
        Componente other = (Componente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Componente[ id=" + id + " ]";
    }
    
}
