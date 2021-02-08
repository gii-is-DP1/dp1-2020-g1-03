<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vacunas">

    <h2>Informacion sobre vacuna</h2>

    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${vacuna.tipoEnfermedad.name}"/></b></td>
        </tr>
        <tr>
            <th>Fecha</th>
            <td><c:out value="${vacuna.fecha}"/></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${vacuna.descripcion}"/></td>
        </tr>
        <tr>
            <th>Nombre del Veterinario</th>
            <td><c:out value="${vacuna.vet.firstName} ${vacuna.vet.lastName}"/></td>
        </tr>
        <tr>
            <th>Nombre de la Mascota</th>
            <td><c:out value="${vacuna.pet.name}"/></td>
        </tr>
        <tr>
            <th>Nombre del Owner</th>
            <td><c:out value="${vacuna.pet.owner.firstName} ${vacuna.pet.owner.lastName}"/></td>
        </tr>
    </table>
    
    <spring:url value="/vets/vacuna" var="vacunasUrl">          	
                    </spring:url>
                    <a href="${fn:escapeXml(vacunasUrl)}" class="btn btn-default"><c:out value="Volver"/></a>


    <br/>
    <br/>
    <br/>


</petclinic:layout>