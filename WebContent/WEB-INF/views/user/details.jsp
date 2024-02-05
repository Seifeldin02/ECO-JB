<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style>
<%@include file="/styles/dashboard.css"%>
.dashboard-container {
    display: flex;
    justify-content: space-between;
}
.content {
    display: flex;
    flex-direction: column;
    width: 80%;
}
.user-details, .carbon-details {
    border: 1px solid #ddd;
    padding: 20px;
    margin-bottom: 20px;
}
.user-details h2, .carbon-details h2 {
    margin-top: 0;
}
</style>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Dashboard
            </div>
<div class="user-details">
    <h2>Your Carbon Emission Details</h2>
    <c:if test="${carbon != null}">
        <h3>Water Details</h3>
        <p><strong>Usage:</strong> ${carbon.waterUsage != null ? carbon.waterUsage : 'N/A'} <%= "m<sup>3</sup>" %></p>
        <p><strong>Bill:</strong> ${carbon.waterBill != null ? carbon.waterBill : 'N/A'} MYR</p>
        <p><strong>Days:</strong> ${carbon.waterDays != null ? carbon.waterDays : 'N/A'} Days</p>
        <p><strong>Date:</strong> ${carbon.waterDate != null ? carbon.waterDate : 'N/A'}</p>

        <h3>Electric Details</h3>
        <p><strong>Usage:</strong> ${carbon.electricUsage != null ? carbon.electricUsage : 'N/A'} kWh</p>
        <p><strong>Bill:</strong> ${carbon.electricBill != null ? carbon.electricBill : 'N/A'} MYR</p>
        <p><strong>Days:</strong> ${carbon.electricDays != null ? carbon.electricDays : 'N/A'} Days</p>
        <p><strong>Date:</strong> ${carbon.electricDate != null ? carbon.electricDate : 'N/A'}</p>

        <h3>Recycle Details</h3>
        <p><strong>Usage:</strong> ${carbon.recycleUsage != null ? carbon.recycleUsage : 'N/A'} kg</p>
        <p><strong>Bill:</strong> ${carbon.recycleBill != null ? carbon.recycleBill : 'N/A'} MYR</p>
        <p><strong>Days:</strong> ${carbon.recycleDays != null ? carbon.recycleDays : 'N/A'} Days</p>
        <p><strong>Date:</strong> ${carbon.recycleDate != null ? carbon.recycleDate : 'N/A'}</p>
    </c:if>
            </div>
            <c:if test="${not empty message}">
                <p class="message">${message}</p>
            </c:if>
        </div>
    </div>
</body>
</html>