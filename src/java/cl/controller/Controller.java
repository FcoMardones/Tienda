/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.controller;

import cl.model.ServicioLocal;
import java.io.IOException;
import cl.entities.*;
import cl.model.TransactionException;
import java.io.InputStream;
import java.util.*;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import sun.misc.IOUtils;

/**
 *
 * @author HP EliteBook
 */
@WebServlet(name = "Controller", urlPatterns = {"/control.do"})
@MultipartConfig(location ="/tmp",
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*5*5
)
public class Controller extends HttpServlet {

    @EJB
    private ServicioLocal servicio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bt = request.getParameter("bt");
        switch (bt) {
            case "addcat":
                addcat(request, response);
                break;
            case "editcat":
                editcat(request, response);
                break;
            case "adduser":
                adduser(request, response);
                break;
            case "addprod":
                addprod(request, response);
                break;
            case "editprodes":
                editprodes(request, response);
                break;
            case "iniciar":
                iniciarSesion(request, response);
                break;
            case "addcar":
                addcar(request, response);
                break;
            case "deletecar":
                deletecar(request, response);
                break;
            case "compra":
                compra(request, response);
            break;
        }
    }
    
    
    protected void compra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut =request.getParameter("rut");
        Usuario user = servicio.buscarUsuario(rut);
        if (user !=null & user.getTipo().equals("cliente") ){
            
        ArrayList<Producto> carro = (ArrayList) request.getSession().getAttribute("carro");
        ArrayList<String> data = new ArrayList<>();
        for(Producto p:carro){
        
            String unidad = request.getParameter("unidades"+ p.getCodigoproducto());
            data.add(p.getCodigoproducto()+","+unidad);
        }
                
            try {
                servicio.compra(rut, data);
                //Limpiar el carrito
                 request.setAttribute("msg", "Compra Realizada con Exito");
            request.getRequestDispatcher("detallecarro.jsp").forward(request, response);
            } catch (TransactionException ex) {
                sincronizarCarrito(request, response);
               request.setAttribute("msg", "Hubo un error de Stock en su compra Verifique su carrito");
               //Actualizar el carro con la informacion de la bdd
            }
        }else{
            request.setAttribute("msg", "usuario no registrado");
            request.getRequestDispatcher("detallecarro.jsp").forward(request, response);
        
        
        }
    }

    protected void deletecar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        Producto p = servicio.buscarProducto(Integer.parseInt(codigo));
        ArrayList<Producto> carro = (ArrayList) request.getSession().getAttribute("carro");
        carro.remove(p);
        request.getSession().setAttribute("carro", carro);
        response.sendRedirect("detallecarro.jsp");
    }

    protected void addcar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        Producto p = servicio.buscarProducto(Integer.parseInt(codigo));
        ArrayList<Producto> carro = (ArrayList) request.getSession().getAttribute("carro");
        if (carro == null) {
            carro = new ArrayList<>();
        }

        if (!carro.contains(p)) {
            carro.add(p);
            request.getSession().setAttribute("carro", carro);
        }
        response.sendRedirect("venta.jsp");
    }

    protected void iniciarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut = request.getParameter("rut");
        String clave = request.getParameter("clave");

        Usuario user = servicio.iniciarsesion(rut, Hash.md5(clave));
        if (user != null) {
            if (user.getTipo().equals("admin")) {
                request.getSession().setAttribute("admin", user);
                response.sendRedirect("admin.jsp");
            } else if (user.getTipo().equals("vendedor")) {
                request.getSession().setAttribute("admin", user);
                response.sendRedirect("vendedor.jsp");
            } else {
                request.getSession().setAttribute("cliente", user);
                response.sendRedirect("cliente.jsp");
            }
        } else {
            request.setAttribute("msg", "Usuario Incorrecto");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    protected void addprod(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String stock = request.getParameter("stock");
        String codcat = request.getParameter("codcat");
        
        InputStream stream=null;
        Part foto = request.getPart("foto");
        if (foto!=null){
        stream = foto.getInputStream();
        
        response.sendRedirect("producto.jsp");
        }
        
     
        Categoria cat = servicio.buscarCategoria(Integer.parseInt(codcat));
        Producto newprod = new Producto();
        newprod.setNombre(nombre);
        newprod.setPrecio(Integer.parseInt(precio));
        newprod.setStock(Integer.parseInt(stock));
        newprod.setEstado(1);
        newprod.setCodigocategoria(cat);
        if (stream!= null ){
        newprod.setFotoProducto(IOUtils.toByteArray(stream));
        }
        servicio.insertar(newprod);
        cat.getProductoList().add(newprod);
        servicio.sincronizar(cat);
        response.sendRedirect("producto.jsp");
    }

    protected void editprodes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cod = request.getParameter("codigo");
        String est = request.getParameter("estado");
        String pre = request.getParameter("precio");
        int codigo = Integer.parseInt(cod);
        int estado = Integer.parseInt(est);
        int precio = Integer.parseInt(pre);
        servicio.editarProducto(codigo, precio, 0, estado);
        response.sendRedirect("producto.jsp");
    }

    protected void adduser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut = request.getParameter("rut");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String tipo = request.getParameter("tipo");
        String clave = request.getParameter("clave");

        //Como la PK no es autoincrementable hay que validar
        if (servicio.buscarUsuario(rut) == null) {
            Usuario newuser = new Usuario();
            newuser.setRut(rut);
            newuser.setNombre(nombre);
            newuser.setApellido(apellido);
            newuser.setEmail(email);
            newuser.setTipo(tipo);
            newuser.setClave(Hash.md5(clave));
            servicio.insertar(newuser);
            response.sendRedirect("usuario.jsp");
        } else {
            request.setAttribute("msg", "Rut ya ingresado");
            request.getRequestDispatcher("usuario.jsp").forward(request, response);
        }
    }

    protected void addcat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String desc = request.getParameter("desc");
        Categoria newcat = new Categoria();
        newcat.setNombre(nombre);
        newcat.setDescripcion(desc);
        newcat.setEstado(1);
        servicio.insertar(newcat);
        response.sendRedirect("categoria.jsp");
    }

    protected void editcat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cod = request.getParameter("codigo");
        String est = request.getParameter("estado");
        int codigo = Integer.parseInt(cod);
        int estado = Integer.parseInt(est);
        servicio.editarCategoria(codigo, estado);
        response.sendRedirect("categoria.jsp");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
protected void sincronizarCarrito (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    ArrayList<Producto> carro = (ArrayList<Producto>) request.getSession().getAttribute("carro");
    ArrayList<Producto> carro2 = new ArrayList<Producto>();
    for (Producto p : carro) {
        Producto pp= servicio.buscarProducto(p.getCodigoproducto());
        carro2.add(pp);
        
    }
    request.getSession().setAttribute("carro", carro2);
}
}
