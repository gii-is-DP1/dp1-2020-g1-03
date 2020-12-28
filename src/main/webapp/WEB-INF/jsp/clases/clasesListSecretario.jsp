<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>



<petclinic:layout pageName="clases">

  <h2>Clases</h2>



  <table id="claseTable" class="table table-striped">

    <thead>

    <tr>

      <th>Nombre</th>

      <th>Fecha Inicio</th>

      <th>Fecha Fin</th>

      <th>Plazas disponibles</th>

      <th>Tipo Mascota</th>

      <th></th>

    </tr>

    </thead>

    <tbody>

    <c:forEach items="${clases}" var="clase">

      <tr>

        <td>

        	<c:out value="${clase.name}"/>

        </td>

        <td>

          <c:out value="${clase.fechaHoraInicio}"/>

        </td>

        <td>

          <c:out value="${clase.fechaHoraFin}"/>

        </td>

        <td>

          <c:out value="${clase.numeroPlazasDisponibles}"/>

        </td>

        <td>

          <c:out value="${clase.type}"/>

        </td>

        <td>

        	<spring:url value="/secretarios/clases/show/{claseId}" var="claseUrl">

            <spring:param name="claseId" value="${clase.id}"/>

          </spring:url>

          <a href="${fn:escapeXml(claseUrl)}">Mirar detalles de la clase</a>

        </td>

      </tr>

    </c:forEach>

    </tbody>
	
  </table>
<spring:url value="clases/new" var="editUrl">
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Crear clase</a>


</petclinic:layout> 