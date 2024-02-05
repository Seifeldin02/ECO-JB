<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Carbon Details</title>
    <style><%@include file="/styles/dashboard.css"%></style>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns"></script>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Carbon Analysis
            </div>
            <div class="charts-container">
                <div class="chart-box">
                    <h3>Electric Usage</h3>
                    <canvas id="electricChart"></canvas>
                </div>
                <div class="chart-box">
                    <h3>Water Usage</h3>
                    <canvas id="waterChart"></canvas>
                </div>
                <div class="chart-box single">
                    <h3>Recycle Usage</h3>
                    <canvas id="recycleChart"></canvas>
                </div>
                
            </div>
<script>
    var jsonCarbonData = '<c:out value="${jsonCarbonData}" escapeXml="false" />';
    var jsonCurrentUserCarbonData = '<c:out value="${jsonCurrentUserCarbonData}" escapeXml="false" />';
</script>
            <script><%@include file="/js/carbon.js"%></script>
        </div>
    </div>
</body>
</html>