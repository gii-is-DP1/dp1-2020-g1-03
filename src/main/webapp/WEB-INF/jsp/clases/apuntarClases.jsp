<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="apuntarClases">
    <h2>Apuntar Clases</h2>
        
        <form:form modelAttribute="apuntarClase" class="form-horizontal" id="add-clase-form">
        
    	<input type="hidden" name="id" value="${apuntarClase.id}"/>
        <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${apuntarClase.clase.name}"/></b></td>
        </tr>
        <tr>
            <th>Fecha Inicio</th>
            <td><c:out value="${apuntarClase.clase.fechaHoraInicio}"/></td>
        </tr>
        <tr>
            <th>Fecha Fin</th>
            <td><c:out value="${apuntarClase.clase.fechaHoraFin}"/></td>
        </tr>
        <tr>
            <th>Plazas Totales</th>
            <td><c:out value="${apuntarClase.clase.numeroPlazasTotal}"/></td>
        </tr>
        <tr>
            <th>Plazas Disponibles</th>
            <td><c:out value="${apuntarClase.clase.numeroPlazasDisponibles}"/></td>
        </tr>
        <tr>
            <th>Categoria de clase</th>
            <td><c:out value="${apuntarClase.clase.categoriaClase}"/></td>
        </tr>
        <tr>
            <th>Tipo de mascota</th>
            <td><c:out value="${apuntarClase.clase.type}"/></td>
        </tr>
        <tr>
            <th>Adiestrador</th>
            <td><c:out value="${apuntarClase.clase.adiestrador.firstName}"/>  <c:out value=" ${apuntarClase.clase.adiestrador.lastName}"/></td>
        </tr>
        
    </table>
                <div class="control-group">
                    <petclinic:selectField name="pets" label="Mascota" names="${pets}" size="3"/>
                </div>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Apuntar mascota</button>
            </div>
        </div>
    </form:form>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
</petclinic:layout> 