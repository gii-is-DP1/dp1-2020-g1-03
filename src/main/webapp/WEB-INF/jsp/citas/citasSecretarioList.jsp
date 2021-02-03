<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>
	<spring:url value="citas/sinVet" var="sinVetUrl">
    </spring:url>
    <a href="${fn:escapeXml(sinVetUrl)}" class="btn btn-default">Mira el listado de citas sin veterinario</a><br><br>
    <table id="citaTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Estado</th>
            <th>Fecha y hora</th>
            <th>Razon</th>
            <th>Veterinario</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${citas}" var="cita">
            <tr>
            	<td>
                    <c:out value="${cita.estado}"/>
                </td>
                <td>
                    <c:out value="${cita.fechaHora}"/>
                </td>
                <td>
                   <c:out value="${cita.razon}"/>
                </td>
                <td>
                   <c:out value="${cita.vet.firstName} ${cita.vet.lastName}"/>
                </td>
                 <td>
                	<spring:url value="/secretarios/citas/{citaId}" var="citaUrl">
                        <spring:param name="citaId" value="${cita.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(citaUrl)}">Mirar detalles de la cita</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>