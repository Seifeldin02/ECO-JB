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
                <h2>Welcome, staff ${sessionScope.staff.name != null ? sessionScope.staff.name : 'EMPTY'}!</h2>
                <p><strong>Name:</strong> ${sessionScope.staff.name != null ? sessionScope.staff.name : 'EMPTY'}</p>
                <p><strong>Email:</strong> ${sessionScope.staff.email != null ? sessionScope.staff.email : 'EMPTY'}</p>
                <p><strong>IC No:</strong> ${sessionScope.staff.icNo != null ? sessionScope.staff.icNo : 'EMPTY'}</p>
                <p><strong>HP No:</strong> ${sessionScope.staff.hpNo != null ? sessionScope.staff.hpNo : 'EMPTY'}</p>
                <p><strong>Address:</strong> ${sessionScope.staff.address != null ? sessionScope.staff.address : 'EMPTY'}</p>
                <p><strong>DOB:</strong> ${sessionScope.staff.dob != null ? sessionScope.staff.dob : 'EMPTY'}</p>
                <p><strong>Home Type:</strong> ${sessionScope.staff.homeType != null ? sessionScope.staff.homeType : 'EMPTY'}</p>
                <p><strong>Electricity Bill:</strong> ${sessionScope.staff.electricityBill != null ? sessionScope.staff.electricityBill : 'EMPTY'}</p>
                <p><strong>Water Bill:</strong> ${sessionScope.staff.waterBill != null ? sessionScope.staff.waterBill : 'EMPTY'}</p>
                <p><strong>Recycle Waste:</strong> ${sessionScope.staff.recycleWaste != null ? sessionScope.staff.recycleWaste : 'EMPTY'}</p>
                <p><strong>Status:</strong> ${sessionScope.staff.status != null ? sessionScope.staff.status : 'Unverified'}</p>
            </div>
<c:if test="${not empty message}">
    <p class="message error">${message}</p>
</c:if>  

</div>
    </div>
</body>
</html>