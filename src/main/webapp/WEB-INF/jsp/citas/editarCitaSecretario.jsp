<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="citas">

	<h2>
		<c:if test="${citaMascota['new']}"> Nueva </c:if>
		Cita
	</h2>


	<form:form modelAttribute="cita" class="form-horizontal" id="add-cita-form">
		<div class="form-group has-feedback">
			<div class="control-group">
				<petclinic:selectField label="Estado" name="estado" names="${estados}" size="${estados}" />
			</div>
		    <petclinic:inputField label="Nombre" name="titulo"/>
         	<petclinic:inputField label="Fecha y hora" name="fechaHora"/>
        	<petclinic:inputField label="Razon" name="razon"/>
			<div class="control-group">
				<petclinic:selectField label="Veterinario" name="vet" names="${vets}" size="${vets}" />
			</div>
		</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">

					<button class="btn btn-default" type="submit">Editar cita</button>

				</div>
			</div>		
	</form:form>




</petclinic:layout>