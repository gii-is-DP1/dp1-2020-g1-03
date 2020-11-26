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
                	<spring:url value="/comentarios/" var="comentarioUrl">
                    </spring:url>
                    <c:out value="${comentario.titulo}"/>
                </td>
                <td>
                    <c:out value="${comentario.cuerpo}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>