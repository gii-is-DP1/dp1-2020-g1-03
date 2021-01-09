<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="competiciones">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#fechaHoraInicio").datepicker({
											dateFormat : "dd/mm/yy"
										});
										$("#fechaHoraFin").datepicker({
											dateFormat : "dd/mm/yy"
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
	<h2>
		<c:if test="${competicion['new']}"> Nuevo </c:if>
		Competicion
	</h2>


	<form:form modelAttribute="competicion" class="form-horizontal"
			id="add-competicion-form">
		<div class="form-group has-feedback">
			<petclinic:inputField label="Nombre" name="nombre" />
			<petclinic:inputField label="Cantidad" name="cantidad" />
			<petclinic:inputField label="Fecha de inicio" name="fechaHoraInicio"/>
			<petclinic:inputField label="Fecha de finalizacion" name="fechaHoraFin" />
			<petclinic:inputField label="Premios" name="premios" />
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${competicion['new']}">
						<button class="btn btn-default" type="submit">Crear
							competicion</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit">Editar
							competicion</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>
	</jsp:body>
</petclinic:layout>
