<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style>
<%@include file="/styles/dashboard.css"%>
/* Add some additional styling */

/* Add some additional styling */
.user-details form {
    margin-bottom: 20px;
    max-width: 600px;
    margin: 0 auto;
}
.user-details label {
    display: block;
    margin-bottom: 5px;
    font-size: 1.2em;
}
.user-details input[type="text"], .user-details input[type="date"] {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 1em;
}
.user-details input[type="submit"] {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 1.2em;
    display: block;
    margin: 0 auto;
    border-radius: 4px;
}

    #sidebar {
        position: fixed; /* Change from absolute to fixed */
        height: 100vh;
        z-index: 1; /* Add this line */
        overflow: auto; /* Add this line */
    }

    .content {
        margin-left: 400px; /* Add this line, adjust value as per sidebar width */
    }
</style>
</head>
<body>

    <div class="dashboard-container">
    <div id="navigation">
<div id="sidebar">
    <button id="toggle-sidebar"></button>
    <div class="logo">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
    </div>
    <ul class="menu">
        <li class="profile"><a class="nav-link" href='<c:url value="/user/profile"/>'>Profile</a></li>
        <li class="calendar"><a class="nav-link" href='<c:url value="/user/calendar"/>'>Calendar</a></li>
        <li class="dashboard">
            <a class="nav-link" href='<c:url value="/user/dashboard"/>'>Dashboard</a>
            <ul class="submenu">
                <li><a href='<c:url value="/user/carbon"/>'>Carbon Analysis</a></li>
                <li><a href='<c:url value="/user/details"/>'>My Details</a></li>
                <li><a href='<c:url value="/user/join"/>'>Join us!</a></li>
            </ul>
        </li>
        <li class="logout"><a class="nav-link" href='<c:url value="/user/logout"/>'>Sign Out</a></li>
    </ul>
<script><%@include file="/js/navigation.js"%></script>
</div>        </div>
        <div class="content">
            <div class="header">
                Carbon Footprint Submission
            </div>
            
                        <div class="user-details">
                <h2>Submit Your Carbon Footprint Information</h2>

                <form action="submitFootprints" method="post">
                    <h3>Water Footprint</h3>
                    <label for="waterUsage">Water Usage:</label>
                    <input type="text" id="waterUsage" name="waterUsage" placeholder="Enter your water usage in liters" pattern="\d*\.?\d*" required>

                    <label for="waterBill">Water Bill:</label>
                    <input type="text" id="waterBill" name="waterBill" placeholder="Enter your water bill in dollars" pattern="\d*\.?\d*" required>

                    <label for="waterDays">Water Days:</label>
                    <input type="text" id="waterDays" name="waterDays" placeholder="Enter the number of days for this bill" pattern="\d*" required>

                    <label for="waterDate">Date:</label>
                    <input type="date" id="waterDate" name="waterDate" required>

                    <h3>Electric Footprint</h3>
                    <label for="electricUsage">Electric Usage:</label>
                    <input type="text" id="electricUsage" name="electricUsage" placeholder="Enter your electric usage in kWh" pattern="\d*\.?\d*" required>

                    <label for="electricBill">Electric Bill:</label>
                    <input type="text" id="electricBill" name="electricBill" placeholder="Enter your electric bill in dollars" pattern="\d*\.?\d*" required>

                    <label for="electricDays">Electric Days:</label>
                    <input type="text" id="electricDays" name="electricDays" placeholder="Enter the number of days for this bill" pattern="\d*" required>

                    <label for="electricDate">Date:</label>
                    <input type="date" id="electricDate" name="electricDate" required>

                    <h3>Recycle Footprint</h3>
                    <label for="recycleUsage">Recycle Usage:</label>
                    <input type="text" id="recycleUsage" name="recycleUsage" placeholder="Enter your recycle usage in kg" pattern="\d*\.?\d*" required>
                    
                                       
                    <label for="recycleBill">Recycle Bill:</label>
                    <input type="text" id="recycleBill" name="recycleBill" placeholder="Enter your recycle bill in dollars" pattern="\d*\.?\d*" required>

                    <label for="recycleDays">Recycle Days:</label>
                    <input type="text" id="recycleDays" name="recycleDays" placeholder="Enter the number of days for this usage" pattern="\d*" required>
 

                    <label for="recycleDate">Date:</label>
                    <input type="date" id="recycleDate" name="recycleDate" required>

                    <input type="submit" value="Submit All Footprints">
                </form>
                        <c:if test="${not empty message}">
    <p class="message error">${message}</p>
</c:if>  
            </div>
        </div>
    </div>
</body>
</html>