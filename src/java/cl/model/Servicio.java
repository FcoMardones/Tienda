/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import cl.entities.*;

/**
 *
 * @author HP EliteBook
 */
@Stateless
public class Servicio implements ServicioLocal {

    @PersistenceContext(unitName = "GestionVentaPU")
    private EntityManager em;

    @Override
    public void insertar(Object o) {
        em.persist(o); 
    }

    @Override
    public void sincronizar(Object o) {
        em.merge(o); //actualiza un registro del modelo ORM al MER
        em.flush(); //sincroniza la comunicacion de los datos entre el ORM y el modelo de datos
    }

    @Override
    public Categoria buscarCategoria(int codigo) {
        return em.find(Categoria.class, codigo);
    }

    @Override
    public void editarCategoria(int codigo, int estado) {
        Categoria cat = buscarCategoria(codigo);
        cat.setEstado(estado);
        em.merge(cat);
        em.flush();
        em.refresh(cat);
    }

    @Override
    public List<Categoria> getCategorias() {
        return em.createQuery("select c from Categoria c").getResultList();
    }

    @Override
    public Usuario buscarUsuario(String rut) {
        return em.find(Usuario.class, rut);
    }

    @Override
    public void editarUsuario(String rut, String clave) {
        Usuario user = buscarUsuario(rut);
        user.setClave(clave);
        em.merge(user);
        em.flush();
        em.refresh(user);
    }

    @Override
    public List<Usuario> getUsuarios() {
        return em.createQuery("select u from Usuario u").getResultList();
    }

    @Override
    public Producto buscarProducto(int codigo) {
        return em.find(Producto.class, codigo);
    }

    @Override
    public void editarProducto(int codigo, int precio, int stock, int estado) {
        Producto p = buscarProducto(codigo);
        p.setPrecio(precio);
        p.setStock(p.getStock()+stock);
        p.setEstado(estado);
        em.merge(p);
        em.flush();
        em.refresh(p);
    }

    @Override
    public List<Producto> getProductos() {
        
        return em.createQuery("select p from Producto p").getResultList();
        
    }

    @Override
    public Usuario iniciarsesion(String rut, String clave) {
        Usuario user = buscarUsuario(rut);
        return (user!=null && user.getClave().equals(clave))?user:null;
    }

  

   

    @Override
    public void compra(String rut, ArrayList<String> lista) throws TransactionException {
        ArrayList<Detalle> detallelist= new ArrayList<>();
        
        
        
        Usuario user = buscarUsuario(rut);
        Venta newventa=new Venta();
        newventa.setRutcliente(user);
        newventa.setEstado(1);
        newventa.setFecha(new Date());
       int codigo,unidad, suma=0;
        for (String s : lista) {
            String []x= s.split(",");
            codigo = Integer.parseInt(x[0]);
            unidad = Integer.parseInt(x[1]);
            
            
            Producto p =buscarProducto(codigo);
            //Si no se tiene Stock Se cancela la compra
            if (p.getStock()<unidad){
                
                throw new TransactionException();
            }
            p.setStock(p.getStock()-unidad);
            em.merge(p);
            
            Detalle newdetalle = new Detalle();
            newdetalle.setCodigoproducto(p);
            newdetalle.setCodigoventa(newventa);
            newdetalle.setEstado(1);
            newdetalle.setPrecio(p.getPrecio());
            newdetalle.setStock(unidad);
            
            
            detallelist.add(newdetalle);
            
            suma += p.getPrecio()+unidad;
        }
        newventa.setTotal(suma);
        em.persist(newventa);
        //asociar la venta con el detalle de la venta
        newventa.setDetalleList(detallelist);
        
        em.merge(newventa);
        
        //asociar el usuario con la venta
        user.getVentaList().add(newventa);  
        em.merge(user);
        
        //Sincronizar
        em.flush();
        
        
    }

    
    
}
