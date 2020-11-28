<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="comentarios">
        
        <h2>
         <c:if test="${comentario['new']}"> Nuevo </c:if>  Comentario
    	</h2>
    	
        
        <form:form modelAttribute="comentario" class="form-horizontal" id="add-comentario-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Titulo" name="titulo"/>
            <petclinic:inputField label="Cuerpo" name="cuerpo"/>
            <petclinic:inputField label="Vet" name="vet"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${comentario['new']}">
                        <button class="btn btn-default" type="submit">Crear comentario</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Editar comentario</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        </form:form>

        <%-- <c:forEach items="${comentarios}" var="comentario">
            <tr>
                <td>
                	
                    <c:out value="${comentario.titulo}"/>
                </td>
                <td>
                    <c:out value="${comentario.cuerpo}"/>
                </td>
                <td>
                    
    	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar comentario</a>
                </td>
            </tr>
        </c:forEach>  --%>
        
        
    
    
</petclinic:layout>

