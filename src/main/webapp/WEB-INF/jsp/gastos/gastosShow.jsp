<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="gastos">

    <h2>Informacion sobre gastos</h2>


    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${gasto.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Cantidad</th>
            <td><c:out value="${gasto.cantidad} EUR"/></td>
        </tr>
        <tr>
            <th>Fecha</th>
            <td><c:out value="${gasto.fecha}"/></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${gasto.description}"/></td>
        </tr>
    </table>
    <spring:url value="{gastoId}/edit" var="editUrl">
        <spring:param name="gastoId" value="${gasto.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar gasto</a>

    <br/>
    <br/>
    <br/>


</petclinic:layout>
