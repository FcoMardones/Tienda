/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.model;

import javax.ejb.Local;
import java.util.*;
import cl.entities.*;

/**
 *
 * @author HP EliteBook
 */
@Local
public interface ServicioLocal {
    void insertar(Object o);
    void sincronizar(Object o);
    
    
            
    //Modulo Categoria
    Categoria buscarCategoria(int codigo);
    void editarCategoria(int codigo, int estado);
    List<Categoria> getCategorias();
    //Modulo Usuario
    Usuario buscarUsuario(String rut);
    void editarUsuario(String rut, String clave);
    List<Usuario> getUsuarios();
    //Modulo Producto
    Producto buscarProducto(int codigo);
    void editarProducto(int codigo, int precio, int stock, int estado);
    List<Producto> getProductos();
    
    
    //Iniciar Sesion
    Usuario iniciarsesion(String rut, String clave);
    
    void compra(String rut, ArrayList<String> lista) throws TransactionException;
    
    
}
