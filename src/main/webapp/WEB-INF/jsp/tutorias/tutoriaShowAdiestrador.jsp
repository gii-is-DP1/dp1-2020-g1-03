<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="tutorias">

    <h2>Informacion sobre tutoria</h2>

    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${tutoria.name}"/></b></td>
        </tr>
        <tr>
            <th>Fecha y Hora</th>
            <td><c:out value="${tutoria.fechaHora}"/></td>
        </tr>
        <tr>
            <th>Razon</th>
            <td><c:out value="${tutoria.razon}"/></td>
        </tr>
        <tr>
            <th>Nombre del Dueño</th>
            <td><c:out value="${tutoria.pet.owner.firstName} ${tutoria.pet.owner.lastName}"/></td>
        </tr>
        <tr>
            <th>Nombre de la Mascota</th>
            <td><c:out value="${tutoria.pet.name}"/></td>
        </tr>
    </table>

	<spring:url value="{tutoriaId}/edit" var="editUrl">
        <spring:param name="tutoriaId" value="${tutoria.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar tutoria</a>

	<spring:url value="/adiestradores/tutorias" var="tutoriasUrl">          	
                    </spring:url>
                    <a href="${fn:escapeXml(tutoriasUrl)}"><c:out value="Volver"/></a>
                    
</petclinic:layout>