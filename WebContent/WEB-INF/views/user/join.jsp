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
                Join Us
            </div>
    <div class="user-details">
                <h2>Confirm Your Information</h2>
                <p>Confirm that these are your accurate credentials according to your government papers, if not please change the appropriate information in your profile page before submission</p>
                <form id="join" action="<c:url value='/user/confirm'/>"method="post" enctype="multipart/form-data"  >
<p class="editable"><strong>Name:</strong> ${sessionScope.user.name}</p>
<p class="editable"><strong>Email:</strong> ${sessionScope.user.email}</p>
<p class="editable"><strong>IC No:</strong> ${sessionScope.user.icNo}</p>
<p class="editable"><strong>HP No:</strong> ${sessionScope.user.hpNo}</p>
<p class="editable"><strong>Address:</strong> ${sessionScope.user.address}</p>
<p class="editable"><strong>DOB:</strong> ${sessionScope.user.dob}</p>
<p><strong>Home Type:</strong> ${sessionScope.user.homeType}</p>
<p><strong>Upload Credentials (Max 3 files):</strong> 
    <input type="file" id="credentials" name="credentials" multiple>
    <span id="fileError" style="color: red;"></span>
</p>

<script>
    document.getElementById('credentials').onchange = function () {
        var files = this.files;
        var fileError = document.getElementById('fileError');

        if (files.length > 3) {
            fileError.textContent = 'You can only upload a maximum of 3 files.';
            this.value = ''; // Clear the input field
        } else {
            fileError.textContent = '';
        }
    };
</script>                    
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
        
<p><input type="submit" class = "submit" value="Submit"></p>
                </form>
            </div>
        </div>
    </div>
    
</body>
</html>