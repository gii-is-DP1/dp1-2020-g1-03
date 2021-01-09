<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">

    <h2>Informacion sobre Mascota</h2>

    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${pet.name}"/></b></td>
        </tr>
        <tr>
            <th>Especie</th>
            <td><c:out value="${pet.type.name}"/></td>
        </tr>
        <tr>
            <th>Fecha nacimiento</th>
            <td><c:out value="${pet.birthDate}"/></td>
        </tr>
        <tr>
            <th>Nombre del Dueño</th>
            <td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td>
        </tr>
    </table>
	
	<spring:url value="/vets/vacuna/pets/{petId}/create" var="createUrl">
		<spring:param name="petId" value="${pet.id}"/>
    </spring:url>
   	<a href="${fn:escapeXml(createUrl)}" class="btn btn-default">Andir nueva vacuna</a>

    <br/>
    <br/>
    <br/>


</petclinic:layout>