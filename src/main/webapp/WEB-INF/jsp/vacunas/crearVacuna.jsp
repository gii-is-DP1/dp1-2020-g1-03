<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vacunas">
    <h2>
        <c:if test="${vacuna['new']}">Nueva </c:if> Vacuna
    </h2>
    <form:form modelAttribute="vacuna" class="form-horizontal" id="add-vacuna-form">
    	<input type="hidden" name="id" value="${vacuna.id}"/>
        <div class="form-group has-feedback">
       <div class="control-group">
           <div class="form-group">
                    <label class="col-sm-2 control-label">Mascota</label>
                    <div class="col-sm-10">
                        <c:out value="${vacuna.pet.name}"/>
                    </div>
                </div>
                    <petclinic:selectField name="tipoEnfermedad" label="Nombre" names="${tipoenfermedades}" size="3"/>
                </div>
            <petclinic:inputField label="Fecha" name="fecha"/>
            <petclinic:inputField label="Descripcion" name="descripcion"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vacuna['new']}">
                        <button class="btn btn-default" type="submit">Crear vacuna</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>