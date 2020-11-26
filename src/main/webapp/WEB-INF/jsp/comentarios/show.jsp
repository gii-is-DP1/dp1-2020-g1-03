<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="comentarios">

    <h2>Informacion sobre comentarios</h2>
		<h2>
         <c:if test="${comentario['new']}"> Nuevo </c:if>  Comentario
    	</h2>
    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${comentario.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Cuerpo</th>
            <td><c:out value="${comentario.cuerpo}"/></td>
        </tr>
    </table>
    <spring:url value="/owners/edit/{comentarioId}" var="editUrl">
        <spring:param name="comentarioId" value="${comentario.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar comentario</a>

    <br/>
    <br/>
    <br/>


</petclinic:layout>