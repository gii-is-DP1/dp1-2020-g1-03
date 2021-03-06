<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vacunas">
    <h2>
        <c:if test="${tutoria['new']}">Nueva </c:if> Tutoria
    </h2>
    <form:form modelAttribute="tutoria" class="form-horizontal" id="add-tutoria-form">
    	<input type="hidden" name="id" value="${tutoria.id}"/>
    	<div class="form-group has-feedback">
       <div class="control-group">
           <div class="form-group">
                    <label class="col-sm-2 control-label">Mascota</label>
                    <div class="col-sm-10">
                        <c:out value="${tutoria.pet.name}"/>
                    </div>
            </div>
            </div>
            <petclinic:inputField label="Titulo" name="titulo"/>
            <petclinic:inputField label="Fecha" name="fechaHora"/>
            <petclinic:inputField label="Razon" name="razon"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${tutoria['new']}">
                        <button class="btn btn-default" type="submit">Crear tutoria</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Editar tutoria</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>