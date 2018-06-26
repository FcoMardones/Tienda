<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="cl.entities.*"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="cl.model.ServicioLocal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%! ServicioLocal servicio;%>
<%
    InitialContext ctx = new InitialContext();
    servicio = (ServicioLocal) ctx.lookup("java:global/GestionVenta/Servicio!cl.model.ServicioLocal");
    List<Producto> listap = servicio.getProductos();
%>

<c:set scope="page" var="listap" value="<%=listap%>"/>
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
                <c:forEach items="${listap}" var="p">
                    <c:if test="${p.estado eq 1 and p.stock > 0}">
                        <div class="col s3">
                            <div class="card-panel">
                                <form action="control.do" method="post">
                                    <input type="hidden" name="codigo" value="${p.codigoproducto}"/>
                                    <p>${p.nombre}</p>
                                    <h5>${p.precio}</h5>
                                    <button class="btn-floating right" type="submit" name="bt" value="addcar">
                                        <i class="material-icons">add</i>
                                    </button>
                                    <br><br>
                                </form>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
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
