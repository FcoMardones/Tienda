
<%@taglib prefix="xx" uri="/WEB-INF/tlds/TAGiMG.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="cl.entities.Categoria"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="cl.model.ServicioLocal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="categorias" scope="page"
       value="<%=servicio.getCategorias()%>"/>
<c:set var="productos" scope="page"
       value="<%=servicio.getProductos()%>"/>
       
       

<%! ServicioLocal servicio;%>
<%
    InitialContext ctx = new InitialContext();
    servicio = (ServicioLocal) ctx.lookup("java:global/GestionVenta/Servicio!cl.model.ServicioLocal");
    List<Categoria> lista = servicio.getCategorias();
%>
<c:set scope="page" var="lista" value="<%=lista%>"/>
<!DOCTYPE html>
<html>
    <head>
        <!--Import Google Icon Font-->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!--Import materialize.css-->
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>

        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>

    <body>

        <c:if test="${not empty admin}">


            <c:import url="menu.jsp"/>


            <div class="row">
                <div class="col s6">
                    <h3>CATEGORIAS</h3>
                    <form action="control.do" method="POST">
                        <div class="input-field">
                            <input id="nombre" type="text" name="nombre">
                            <label for="nombre">Nombre</label>
                        </div>
                        <div class="input-field">
                            <input id="desc" type="text" name="desc">
                            <label for="desc">Descripcion</label>
                        </div>
                        <button class="btn right" name="bt" value="addcat" type="submit">Guardar</button>
                    </form>
                    <br><br>
                    <table class="bordered">
                        <thead>
                        <tr>
                            <td>CODIGO</td>
                            <td>NOMBRE</td>
                            <td>ESTADO</td>
                            <th>Foto</th>
                            <td></td>
                        </tr>
                        </thead>
                        <c:forEach items="${lista}" var="c">
                            <tr>
                                <td>${c.codigocategoria}</td>
                                <td>${c.nombre}</td>
                                <td>
                                    <c:if test="${c.estado eq 1}">
                                        ACTIVADO
                                    </c:if>
                                    <c:if test="${c.estado eq 0}">
                                        DESACTIVADO
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${c.estado eq 1}">
                                        <a href="control.do?bt=editcat&codigo=${c.codigocategoria}&estado=0" class="btn-floating red">
                                            <i class="material-icons">star_border</i>
                                        </a>
                                    </c:if>
                                    <c:if test="${c.estado eq 0}">
                                        <a href="control.do?bt=editcat&codigo=${c.codigocategoria}&estado=1" class="btn-floating green">
                                            <i class="material-icons">star_border</i>
                                        </a>
                                    </c:if>
                                    
                                </td>
                                <td>X</td>
                            </tr>
                        </c:forEach>

                    </table>
                    <br>
                   
                </div>
            </div>

        </c:if>
        <c:if test="${empty admin}">
            Error, seras redireccionado en 5 segundos
            <meta http-equiv="refresh" content="5;url=index.jsp">
        </c:if>
        <!--JavaScript at end of body for optimized loading-->
        <script type="text/javascript" src="js/materialize.min.js"></script>
    </body>
</html>
