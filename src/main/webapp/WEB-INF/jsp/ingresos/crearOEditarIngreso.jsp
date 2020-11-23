<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="ingresos">
    <h2>
        <c:if test="${ingreso['new']}">Nuevo </c:if> Ingreso
    </h2>
    <form:form modelAttribute="ingreso" class="form-horizontal" id="add-gasto-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Titulo" name="titulo"/>
            <petclinic:inputField label="Cantidad" name="cantidad"/>
            <petclinic:inputField label="Fecha" name="fecha"/>
            <petclinic:inputField label="Descripcion" name="description"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${ingreso['new']}">
                        <button class="btn btn-default" type="submit">Crear ingreso</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Editar ingreso</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>