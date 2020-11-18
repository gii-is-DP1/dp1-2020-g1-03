<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="ingresos">
    <h2>Ingresos</h2>

    <table id="ingresosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Titulo</th>
            <th>Fecha</th>
            <th>Cantidad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${ingresos}" var="ingreso">
            <tr>
                <td>
                	<spring:url value="/economistas/ingreso/{ingresoId}" var="ingresoUrl">
                        <spring:param name="ingresoId" value="${ingreso.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(ingresoUrl)}"><c:out value="${ingreso.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${ingreso.fecha}"/>
                </td>
                <td>
                    <c:out value="${ingreso.cantidad}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="ingreso/create" var="createUrl">
    </spring:url>
   	<a href="${fn:escapeXml(createUrl)}" class="btn btn-default">Crear ingreso</a>

</petclinic:layout>