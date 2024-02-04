<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style>
<%@include file="/styles/dashboard.css"%>
/* Add some additional styling */
.user-details form {
    margin-bottom: 20px;
}
.user-details label {
    display: block;
    margin-bottom: 5px;
}
.user-details input[type="number"],
.user-details input[type="file"] {
    width: 80%;
    padding: 10px;
    margin-bottom: 10px;
}
.user-details input[type="submit"] {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
}
</style>
</head>
<body>
    <div class="dashboard-container">
    <div id="navigation">
        <jsp:include page="navigation.jsp" />
        </div>
        <div class="content">
            <div class="header">
                Carbon Footprint Submission
            </div>
            
            <div class="user-details">
                <h2>Submit Your Carbon Footprint Information</h2>

                <form action="submitFootprints" method="post" enctype="multipart/form-data">
                    <h3>Water Footprint</h3>
                    <label for="waterUsage">Water Usage:</label>
                    <input type="number" id="waterUsage" name="waterUsage" placeholder="Enter your water usage in liters">

                    <label for="waterBill">Water Bill:</label>
                    <input type="number" id="waterBill" name="waterBill" placeholder="Enter your water bill in dollars">

                    <label for="waterDays">Water Days:</label>
                    <input type="number" id="waterDays" name="waterDays" placeholder="Enter the number of days for this bill">

                    <label for="waterFile">Upload File:</label>
                    <input type="file" id="waterFile" name="waterFile">

                    <h3>Electric Footprint</h3>
                    <label for="electricUsage">Electric Usage:</label>
                    <input type="number" id="electricUsage" name="electricUsage" placeholder="Enter your electric usage in kWh">

                    <label for="electricBill">Electric Bill:</label>
                    <input type="number" id="electricBill" name="electricBill" placeholder="Enter your electric bill in dollars">

                    <label for="electricDays">Electric Days:</label>
                    <input type="number" id="electricDays" name="electricDays" placeholder="Enter the number of days for this bill">

                    <label for="electricFile">Upload File:</label>
                    <input type="file" id="electricFile" name="electricFile">

                    <h3>Recycle Footprint</h3>
                    <label for="recycleUsage">Recycle Usage:</label>
                    <input type="number" id="recycleUsage" name="recycleUsage" placeholder="Enter your recycle usage in kg">

                    <label for="recycleBill">Recycle Bill:</label>
                    <input type="number" id="recycleBill" name="recycleBill" placeholder="Enter your recycle bill in dollars">

                    <label for="recycleDays">Recycle Days:</label>
                    <input type="number" id="recycleDays" name="recycleDays" placeholder="Enter the number of days for this bill">

                    <label for="recycleFile">Upload File:</label>
                    <input type="file" id="recycleFile" name="recycleFile">

                    <input type="submit" value="Submit All Footprints">
                </form>
            </div>
        </div>
    </div>
</body>
</html>