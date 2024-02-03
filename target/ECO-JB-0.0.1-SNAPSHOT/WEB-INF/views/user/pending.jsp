<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style><%@include file="/styles/dashboard.css"%></style>
<style><%@include file="/styles/buttons.css"%></style>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Application
            </div>
    <div class="user-details">
                <h2>Your Application</h2>
                <p>Review your current application, if you want to make changes, delete the application, change your profile accordingly and upload the proper credentials.</p>
                <p><strong>Name:</strong> ${sessionScope.user.name}</p>
                <p><strong>Email:</strong> ${sessionScope.user.email}</p>
                <p><strong>IC No:</strong> ${sessionScope.user.icNo}</p>
                <p><strong>HP No:</strong> ${sessionScope.user.hpNo}</p>
                <p><strong>Address:</strong> ${sessionScope.user.address}</p>
                <p><strong>DOB:</strong> ${sessionScope.user.dob}</p>
                <p><strong>Home Type:</strong> ${sessionScope.user.homeType}</p>
                <p><strong>Uploaded Credentials:</strong></p>
<p><a href="${pageContext.request.contextPath}/${sessionScope.application.file1}" target="_blank" style="color: blue;">${sessionScope.application.file1}</a></p>
<p><a href="${pageContext.request.contextPath}/${sessionScope.application.file2}" target="_blank" style="color: blue;">${sessionScope.application.file2}</a></p>
<p><a href="${pageContext.request.contextPath}/${sessionScope.application.file3}" target="_blank" style="color: blue;">${sessionScope.application.file3}</a></p>
                <form action="<c:url value='/user/deleteApplication'/>" method="post">
                    <input type="submit" class="deleteButton" value="Delete Application">

                </form>
                    <c:if test="${not empty successMessage}">
        <div class="message success">
            ${successMessage}
        </div>
    </c:if>
                    <c:if test="${not empty errorMessage}">
        <div class="message error">
            ${errorMessage}
        </div>
    </c:if>
            </div>
        </div>
    </div>
</body>
</html>