<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="citas">

	<c:choose>
		<c:when test="${cita['new']}">
			<h2>Nueva Cita</h2>
		</c:when>
		<c:otherwise>
			<h2>Editar Cita</h2>
		</c:otherwise>
	</c:choose>


	<form:form modelAttribute="cita" class="form-horizontal"
		id="add-cita-form">
		<div class="form-group has-feedback">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="titulo" />
				<petclinic:inputField label="Fecha y hora" name="fechaHora" />
				<petclinic:inputField label="Razon" name="razon" />

			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${cita['new']}">
						<button class="btn btn-default" type="submit">Crear cita</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit">Editar cita</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>




</petclinic:layout>