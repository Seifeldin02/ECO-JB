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
            <div class="signin-header">Sign In</div>
            <!-- Display the message from the session -->
            <c:if test="${sessionScope.message != null}">
    <div class="message">
        ${sessionScope.message}
    </div>
    <small> <% session.removeAttribute("message"); %></small>
</c:if>
            <form action="<c:url value='/staff/authenticate'/>" method="post">
    <div class="form-group">
        <label for="email" class="input-label">Email:</label>
        <input type="email" class="form-control input-field" id="email" placeholder="Enter email" name="email" required>
    </div>
    <div class="form-group">
        <label for="password" class="input-label">Password:</label>
        <input type="password" class="form-control input-field" id="password" placeholder="Enter password" name="password" required>
    </div>
    <div class="form-group">
        <input type="checkbox" id="rememberMe" name="rememberMe">
        <label for="rememberMe">Remember me</label>
    </div>
    <div class="error-message" style="color:red;">
        <c:if test="${not empty error}">
            <c:out value="${error}"/>
        </c:if>
    </div>
           <div class="message">
        <c:if test="${not empty message}">
            <c:out value="${message}"/>
        </c:if>
    </div>
    <button type="submit" class="button submit-button">Let me in</button>
</form>
            <div class="link-container">
                <a href="<c:url value='/staff/forgotPassword'/>" class="small-link">
                    Forget password?
                </a>
                <a href="<c:url value='/staff/register'/>" class="small-link">
                    Not a staff? Sign up here!
                </a>
            </div>
        </div>
    </div>
</body>
</html>