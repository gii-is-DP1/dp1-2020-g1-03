<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vacunas">
    <h2>Vacunas</h2>

    <table id="vacunasTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vacunas}" var="vacuna">
            <tr>
                <td>
                	<spring:url value="/vets/vacuna/{vacunaId}" var="vacunasUrl">
                	<spring:param name="vacunaId" value="${vacuna.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(vacunasUrl)}"><c:out value="${vacuna.tipoenfermedad.name}"/></a>
                </td>
                <td>
                    <c:out value="${vacuna.fecha}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="vacuna/create" var="createUrl">
    </spring:url>
   	<a href="${fn:escapeXml(createUrl)}" class="btn btn-default">Crear vacuna</a>

</petclinic:layout>
