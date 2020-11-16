<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Titulo</th>
            <th>Fecha</th>
            <th>Cantidad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${gastos}" var="gasto">
            <tr>
                <td>
                	<spring:url value="/economistas/gasto/{gastoId}" var="gastoUrl">
                        <spring:param name="gastoId" value="${gasto.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(gastoUrl)}"><c:out value="${gasto.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${gasto.fecha}"/>
                </td>
                <td>
                    <c:out value="${gasto.cantidad}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="gasto/new" var="editUrl">
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Crear gasto</a>

</petclinic:layout>
