<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="comentarios">

    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${comentario.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Cuerpo</th>
            <td><c:out value="${comentario.cuerpo}"/></td>
        </tr>
        <tr>
            <th>Dueño</th>
            <td><c:out value="${comentario.owner.firstName}"/>,<c:out value="${comentario.owner.lastName}"/></td>
        </tr>
    </table><%-- 
    <spring:url value="/owners/comentarios/edit/{comentarioId}/{vetId}" var="editUrl">
        <spring:param name="comentarioId" value="${comentario.id}"/>
        <spring:param name="vetId" value="${comentario.vet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar comentario</a> --%>

    <br/>
    <br/>
    <br/>


</petclinic:layout>