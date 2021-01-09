<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Competiciones</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Inicio</th>
            <th>Fin</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${competiciones}" var="competicion">
            <tr>
                <td>
                <spring:url value="/secretarios/competiciones/show/{competicionId}" var="competicionUrl">
                        <spring:param name="competicionId" value="${competicion.id}"/>
                    </spring:url>
                	<a href="${fn:escapeXml(competicionUrl)}"><c:out value="${competicion.nombre}"/></a>
                </td>
                <td>
                    <c:out value="${competicion.fechaHoraInicio}"/>
                </td>
                <td>
                    <c:out value="${competicion.fechaHoraFin}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/secretarios/competiciones/new/" var="editUrl">
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Crear competicion</a>

</petclinic:layout>