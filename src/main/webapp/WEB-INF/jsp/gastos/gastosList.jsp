<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Titulo</th>
            <th>Fecha</th>
            <th>Cantidad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${gastos}" var="gasto">
            <tr>
                <td>
                    <c:out value="${gasto.titulo}"/>
                </td>
                <td>
                    <c:out value="${gasto.fecha}"/>
                </td>
                <td>
                    <c:out value="${gasto.cantidad}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>
