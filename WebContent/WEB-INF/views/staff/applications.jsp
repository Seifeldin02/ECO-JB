<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Applications</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    
    <style><%@include file="/styles/dashboard.css"%></style>
    <style><%@include file="/styles/applications.css"%></style>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Applications
            </div>
            <input type="text" id="search" placeholder="Search..." class="form-control mb-3">

            <div id="tables">
 <div class="table-responsive">
    <table id="applicationsTable" class="table table-striped table-bordered table-hover">
        <thead>
    
    <tr>
        <th>Application ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Address</th>
        <th>DOB</th>
        <th>Home Type</th>
        <th>HP No</th>
        <th>IC No</th>
        <th>File 1</th>
        <th>File 2</th>
        <th>File 3</th>
        <th>Approve</th>
        <th>Decline</th> 
    </tr>
    </thead>
    <c:forEach var="application" items="${applications}">
    
        <tr>
            <td>${application.id}</td>
            <td>${application.name}</td>
            <td>${application.email}</td>
            <td>${application.address}</td>
            <td>${application.dob}</td>
            <td>${application.homeType}</td>
            <td>${application.hpNo}</td>
            <td>${application.icNo}</td>
            <td><c:if test="${not empty application.file1}"><a href="${pageContext.request.contextPath}/${application.file1}" target="_blank" style="color: blue;">File 1</a></c:if></td>
            <td><c:if test="${not empty application.file2}"><a href="${pageContext.request.contextPath}/${application.file2}" target="_blank" style="color: blue;">File 2</a></c:if></td>
            <td><c:if test="${not empty application.file3}"><a href="${pageContext.request.contextPath}/${application.file3}" target="_blank" style="color: blue;">File 3</a></c:if></td>
<td>
    <form action="<c:url value='/staff/approveApplication'/>" method="post">
        <input type="hidden" name="id" value="${application.id}">
        <button type="submit" class="btn btn-success">Approve</button>
    </form>
</td>
<td>
    <form action="<c:url value='/staff/declineApplication'/>" method="post">
        <input type="hidden" name="id" value="${application.id}">
        <button type="submit" class="btn btn-danger">Decline</button>
    </form>
</td>
        </tr>
    </c:forEach>
</table>
                </div>

                <div class="table-responsive">
                    <table id="carbonTable" class="table table-striped">
                        <thead>
                            <tr>
                                <!-- Define your Carbon table columns here -->
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Carbon rows will be populated here -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script><%@include file="/js/applications.js"%></script>
</body>
</html>