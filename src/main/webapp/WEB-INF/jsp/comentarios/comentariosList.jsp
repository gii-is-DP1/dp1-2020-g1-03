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
            <th>Cuerpo</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${comentarios}" var="comentario">
            <tr>
                <td>
                	<spring:url value="/vets/comentarios/show/{comentarioId}" var="comentarioUrl">
                        <spring:param name="comentarioId" value="${comentario.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(comentarioUrl)}"><c:out value="${comentario.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${comentario.cuerpo}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>