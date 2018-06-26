/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP EliteBook
 */
@Entity
@Table(name = "detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalle.findAll", query = "SELECT d FROM Detalle d")
    , @NamedQuery(name = "Detalle.findByCodigodetalle", query = "SELECT d FROM Detalle d WHERE d.codigodetalle = :codigodetalle")
    , @NamedQuery(name = "Detalle.findByPrecio", query = "SELECT d FROM Detalle d WHERE d.precio = :precio")
    , @NamedQuery(name = "Detalle.findByStock", query = "SELECT d FROM Detalle d WHERE d.stock = :stock")
    , @NamedQuery(name = "Detalle.findByEstado", query = "SELECT d FROM Detalle d WHERE d.estado = :estado")})
public class Detalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigodetalle")
    private Integer codigodetalle;
    @Column(name = "precio")
    private Integer precio;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "codigoproducto", referencedColumnName = "codigoproducto")
    @ManyToOne
    private Producto codigoproducto;
    @JoinColumn(name = "codigoventa", referencedColumnName = "codigoventa")
    @ManyToOne
    private Venta codigoventa;

    public Detalle() {
    }

    public Detalle(Integer codigodetalle) {
        this.codigodetalle = codigodetalle;
    }

    public Integer getCodigodetalle() {
        return codigodetalle;
    }

    public void setCodigodetalle(Integer codigodetalle) {
        this.codigodetalle = codigodetalle;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Producto getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(Producto codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public Venta getCodigoventa() {
        return codigoventa;
    }

    public void setCodigoventa(Venta codigoventa) {
        this.codigoventa = codigoventa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigodetalle != null ? codigodetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalle)) {
            return false;
        }
        Detalle other = (Detalle) object;
        if ((this.codigodetalle == null && other.codigodetalle != null) || (this.codigodetalle != null && !this.codigodetalle.equals(other.codigodetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.entities.Detalle[ codigodetalle=" + codigodetalle + " ]";
    }
    
}
