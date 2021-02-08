<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="tutorias">
    <h2>Dueño</h2>

    <table id="Table" class="table table-striped">
        <thead>
        <tr>
            <th>Titulo</th>
            <th>Fecha y hora</th>
            <th>Razón</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tutorias}" var="tutoria">
            <tr>
                <td>
                	<spring:url value="/owners/tutorias/show/{tutoriaId}" var="tutoriaUrl">
                        <spring:param name="tutoriaId" value="${tutoria.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(tutoriaUrl)}"><c:out value="${tutoria.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${tutoria.fechaHora}"/>
                </td>
                <td>
                    <c:out value="${tutoria.razon}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
