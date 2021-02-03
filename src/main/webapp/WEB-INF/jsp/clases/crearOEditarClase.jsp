<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="clases">
        
        <h2>
         <c:if test="${clase['new']}"> Nueva </c:if>  Clase
    	</h2>
    	
        
        <form:form modelAttribute="clase" class="form-horizontal" id="add-clase-form">
        <div class="form-group has-feedback">
        	<petclinic:inputField label="Nombre" name="name"/>
         	<petclinic:inputField label="Fecha y hora de inicio" name="fechaHoraInicio"/>
        	<petclinic:inputField label="Fecha y hora fin" name="fechaHoraFin"/>
        	<petclinic:inputField label="Numero de plazas total" name="numeroPlazasTotal"/>
        	<petclinic:inputField label="Numero de plazas disponibles" name="numeroPlazasDisponibles"/>
        	<div class="control-group">
                    <petclinic:selectField name="categoriaClase" label="Categoria de la clase" names="${categoriaClase}" size="5"/>
            </div>
        	<div class="control-group">
                    <petclinic:selectField name="type" label="Mascota" names="${types}" size="5"/>
            </div>
        	<div class="control-group">
                    <petclinic:selectField name="adiestrador" label="Adiestrador que la imparte " names="${adiestradores}" size="5"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${clase['new']}">
                        <button class="btn btn-default" type="submit">Crear clase</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Editar clase</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        </form:form>
        
        
    
    
</petclinic:layout>