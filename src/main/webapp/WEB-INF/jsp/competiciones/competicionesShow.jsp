<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="competiciones">

		<h2>
         	<c:if test="${competicion['new']}"> Nuevo </c:if>  Competición
    	</h2>
    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${competicion.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Fecha y hora de inicio</th>
            <td><c:out value="${competicion.fechaHoraInicio}"/></td>
        </tr>
        <tr>
            <th>Fecha y hora de fin</th>
            <td><c:out value="${competicion.fechaHoraFin}"/></td>
        </tr>
        <tr>
            <th>Cantidad</th>
            <td><c:out value="${competicion.cantidad}"/></td>
        </tr>
         <tr>
            <th>Premios</th>
            <td><c:out value="${competicion.premios}"/></td>
        </tr>
    </table>
     <spring:url value="/secretarios/competiciones/edit/{competicionId}" var="editUrl">
        <spring:param name="competicionId" value="${competicion.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar competicion</a>
    <br/>
    <br/>
    <br/>


</petclinic:layout>