<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="clases">

		<h2>
         	<c:if test="${clase['new']}"> Nueva </c:if>  Clase
    	</h2>
    	
    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${clase.name}"/></b></td>
        </tr>
        <tr>
            <th>Fecha Inicio</th>
            <td><c:out value="${clase.fechaHoraInicio}"/></td>
        </tr>
        <tr>
            <th>Fecha Fin</th>
            <td><c:out value="${clase.fechaHoraFin}"/></td>
        </tr>
        <tr>
            <th>Plazas Totales</th>
            <td><c:out value="${clase.numeroPlazasTotal}"/></td>
        </tr>
        <tr>
            <th>Plazas Disponibles</th>
            <td><c:out value="${clase.numeroPlazasDisponibles}"/></td>
        </tr>
        <tr>
            <th>Categoria de clase</th>
            <td><c:out value="${clase.categoriaClase}"/></td>
        </tr>
        <tr>
            <th>Tipo de mascota</th>
            <td><c:out value="${clase.type}"/></td>
        </tr>
        <tr>
            <th>Adiestrador</th>
            <td><c:out value="${clase.adiestrador.firstName}"/>  <c:out value=" ${clase.adiestrador.lastName}"/></td>
        </tr>
        
    
    </table>
<spring:url value="{claseId}/edit" var="editUrl">
        <spring:param name="claseId" value="${clase.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar clase</a>
    
    <spring:url value="{claseId}/delete" var="deleteUrl">
        <spring:param name="claseId" value="${clase.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar clase</a>
    <br/>
    <br/>
    <br/>


</petclinic:layout>