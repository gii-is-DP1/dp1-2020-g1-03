<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="tutorias">

    <h2>Informacion sobre la tutoria</h2>


    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${tutoria.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Fecha y hora</th>
            <td><c:out value="${tutoria.fechaHora} "/></td>
        </tr>
        <tr>
            <th>Razón</th>
            <td><c:out value="${tutoria.razon}"/></td>
        </tr>
    </table>
     <spring:url value="/owners/tutorias" var="tutoriasUrl"></spring:url>
     <a href="${fn:escapeXml(tutoriasUrl)}" class="btn btn-default"><c:out value="Volver"/></a>
    <br/>
    <br/>
    <br/>


</petclinic:layout>