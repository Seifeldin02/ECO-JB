<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<style>
    <%@include file="/styles/dashboard.css"%>
</style>
<script><%@include file="/js/editprofile.js"%></script>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="navigation.jsp" />
        <div class="content">
            <div class="header">
                Profile
            </div>
            
            <!-- Add your profile content here -->
            <div class="user-details">
                <h2>${sessionScope.staff.name != null ? sessionScope.staff.name : 'EMPTY'}'s Profile</h2>
                <form id="editProfileForm" action="<c:url value='/staff/editProfile'/>" method="post" onsubmit="return submitPasswordModal();">
                    <p class="editable"><strong>Name:</strong> ${sessionScope.staff.name != null ? sessionScope.staff.name : 'EMPTY'}</p>
                    <p class="editable"><strong>Email:</strong> ${sessionScope.staff.email != null ? sessionScope.staff.email : 'EMPTY'}</p>
                    <p class="editable"><strong>IC No:</strong> ${sessionScope.staff.icNo != null ? sessionScope.staff.icNo : 'EMPTY'}</p>
                    <p class="editable"><strong>HP No:</strong> ${sessionScope.staff.hpNo != null ? sessionScope.staff.hpNo : 'EMPTY'}</p>
                    <p class="editable"><strong>Address:</strong> ${sessionScope.staff.address != null ? sessionScope.staff.address : 'EMPTY'}</p> 
                    <p class="editable"><strong>DOB:</strong> ${sessionScope.staff.dob != null ? sessionScope.staff.dob : 'EMPTY'}</p>
                    <p class="editable"><strong>Home Type:</strong> ${sessionScope.staff.homeType != null ? sessionScope.staff.homeType : 'EMPTY'}</p>
                    <input type="hidden" id="hiddenPassword" name="password">
                    <p class="status"><strong>Status:</strong> ${sessionScope.staff.status != null ? sessionScope.staff.status : 'Unverified'}</p>
                    <input type="button" id="editButton" value="Edit" onclick="editProfile()" />
                    
                    <input type="submit" id="saveChangesButton" value="Save Changes" style="display: none;" />
                                    <div id="passwordModal" class="modal" style="display: none;">
                                      <div class="modal-content">
                                          <h2>Confirm Password</h2>
    <p>For your security, please confirm your password to continue.</p>
                        <input type="password"  id="password" name="currentPassword" placeholder="Password" >
    
        <div class="button-container">
    
      <button type="button" class="cancel-btn">Cancel</button>
      <button type="submit" class="submit-btn">Confirm</button>                
      </div>
      </div>
                </div>
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
                <p class="warning" id="Warning" style="display: none;">Warning: Editing your profile will set your status to "Unverified". You will be logged out automatically and will have to wait for admin verification before being able to log in again!</p>
                <a href='<c:url value="/staff/join"/>' class="join-us">If you want to get your information verified, submit your application here!</a>
            </div>
        </div>
    </div>
</body>
</html>