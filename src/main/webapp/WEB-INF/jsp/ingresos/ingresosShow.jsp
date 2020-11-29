<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="ingresos">

    <h2>Informacion sobre ingresos</h2>


    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${ingreso.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Cantidad</th>
            <td><c:out value="${ingreso.cantidad} EUR"/></td>
        </tr>
        <tr>
            <th>Fecha</th>
            <td><c:out value="${ingreso.fecha}"/></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${ingreso.description}"/></td>
        </tr>
    </table>
    <spring:url value="{ingresoId}/edit" var="editUrl">
        <spring:param name="ingresoId" value="${ingreso.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar ingreso</a>
    <br/>
    <br/>
    <br/>


</petclinic:layout>