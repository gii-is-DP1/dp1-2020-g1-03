<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">
    <h2>Tutorias</h2>

    <table id="petsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Especie</th>
          	<th>Dueño</th> 
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pets}" var="pet">
            <tr>
                <td>
                	<spring:url value="/adiestradores/tutorias/pets/{petId}/new" var="vacunasUrl">
                	<spring:param name="petId" value="${pet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(vacunasUrl)}"><c:out value="${pet.name}"/></a>
                </td>
                <td>
                    <c:out value="${pet.type}"/>
                </td>
                <td>
                    <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>