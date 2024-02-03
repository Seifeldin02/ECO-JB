<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style><%@include file="/styles/dashboard.css"%></style>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Dashboard
            </div>
            <!-- Add your dashboard content here -->
            <div class="user-details">
                <h2>Welcome, ${sessionScope.user.name != null ? sessionScope.user.name : 'EMPTY'}!</h2>
                <p><strong>Name:</strong> ${sessionScope.user.name != null ? sessionScope.user.name : 'EMPTY'}</p>
                <p><strong>Email:</strong> ${sessionScope.user.email != null ? sessionScope.user.email : 'EMPTY'}</p>
                <p><strong>IC No:</strong> ${sessionScope.user.icNo != null ? sessionScope.user.icNo : 'EMPTY'}</p>
                <p><strong>HP No:</strong> ${sessionScope.user.hpNo != null ? sessionScope.user.hpNo : 'EMPTY'}</p>
                <p><strong>Address:</strong> ${sessionScope.user.address != null ? sessionScope.user.address : 'EMPTY'}</p>
                <p><strong>DOB:</strong> ${sessionScope.user.dob != null ? sessionScope.user.dob : 'EMPTY'}</p>
                <p><strong>Home Type:</strong> ${sessionScope.user.homeType != null ? sessionScope.user.homeType : 'EMPTY'}</p>
                <p><strong>Electricity Bill:</strong> ${sessionScope.user.electricityBill != null ? sessionScope.user.electricityBill : 'EMPTY'}</p>
                <p><strong>Water Bill:</strong> ${sessionScope.user.waterBill != null ? sessionScope.user.waterBill : 'EMPTY'}</p>
                <p><strong>Recycle Waste:</strong> ${sessionScope.user.recycleWaste != null ? sessionScope.user.recycleWaste : 'EMPTY'}</p>
                <p><strong>Status:</strong> ${sessionScope.user.status != null ? sessionScope.user.status : 'Unverified'}</p>
            </div>
<c:if test="${not empty message}">
    <p class="message">${message}</p>
</c:if>  

</div>
    </div>
</body>
</html>