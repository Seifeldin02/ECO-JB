<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style><%@include file="/styles/login.css"%></style>
</head>
<body>
    <div class="login-container">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
        <div class="title">IPRK</div>
        <div class="user-type">I'm a staff</div>
        <div class="login-box">
            <div class="signin-header">Set New Password</div>
            <!-- Display the message from the session -->
            <c:if test="${sessionScope.error != null}">
                <div class="error-message" style="color:red;">
                    ${sessionScope.error}
                </div>
            </c:if>
            <small> <% session.removeAttribute("error"); %></small>
            <form action="<c:url value='/staff/resetPassword'/>" method="post">
                <input type="hidden" name="token" value="${param.token}">
                <div class="form-group">
                    <label for="password" class="input-label">New Password:</label>
                    <input type="password" class="form-control input-field" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="input-label">Confirm New Password:</label>
                    <input type="password" class="form-control input-field" id="confirmPassword" name="confirmPassword" required>
                </div>
                <button type="submit" class="button submit-button">Reset Password</button>
            </form>
            <div class="link-container">
                <a href="<c:url value='/staff/login'/>" class="small-link">
                    Remember password? Sign in here!
                </a>
                <a href="<c:url value='/staff/register'/>" class="small-link">
                    Not a staff? Sign up here!
                </a>
            </div>
        </div>
    </div>
</body>
</html>