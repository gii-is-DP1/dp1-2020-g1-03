<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="cita">

    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${cita.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Fecha y Hora</th>
            <td><c:out value="${cita.fechaHora}"/></td>
        </tr>
        <tr>
            <th>Razón</th>
            <td><c:out value="${cita.razon}"/></td>
        </tr>
        <tr>
            <th>Veterinario</th>
            <td><c:out value="${cita.vet.firstName} ${cita.vet.lastName}"/></td>
        </tr>
        <tr>
            <th>Dueño</th>
            <td><c:out value="${owner.firstName} ${owner.lastName}"/></td>
        </tr>
        <c:forEach items="${mascotas}" var="mascota">
            <tr>
            	<th>Nombre Mascota</th>
                <td>
                    <c:out value="${mascota.name}"/>
                </td>
                <th>Especie</th>
                <td>
                   <c:out value="${mascota.type}"/>
                </td>
            </tr>
        </c:forEach>
        
    </table>
		<!--<c:choose>
		<c:when test="${cita.estado<='PENDIENTE'}">-->
			<div class="form-group">
				<spring:url value="{citaId}/edit" var="editUrl">
        			<spring:param name="citaId" value="${cita.id}"/>
   				</spring:url>
    			<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar cita</a>
			</div>
		<!--</c:when>
		</c:choose>-->    
     <spring:url value="/secretarios/citas" var="citasUrl"></spring:url>
     <a href="${fn:escapeXml(citasUrl)}"><c:out value="Volver"/></a>


</petclinic:layout>