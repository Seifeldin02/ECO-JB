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
        <div class="user-type">I'm a user</div>
        <div class="login-box">
            <div class="signin-header">Forgot Password</div>
            <!-- Display the message from the session -->
<c:if test="${sessionScope.message != null}">
    <div class="message">
        ${sessionScope.message}
    </div>
</c:if>
<small> <% session.removeAttribute("message"); %></small>
            <form action="<c:url value='/user/sendPasswordReset'/>" method="post">
                <div class="form-group">
                    <label for="email" class="input-label">Email:</label>
                    <input type="email" class="form-control input-field" id="email" placeholder="Enter email" name="email" required>
                </div>
                <div class="error-message" style="color:red;">
                    <c:if test="${not empty error}">
                        <c:out value="${error}"/>
                    </c:if>
                </div>
                <button type="submit" class="button submit-button">Send Password Reset Link</button>
            </form>
            <div class="link-container">
                <a href="<c:url value='/user/login'/>" class="small-link">
                    Remember password? Sign in here!
                </a>
                <a href="<c:url value='/user/register'/>" class="small-link">
                    Not a member? Sign up here!
                </a>
            </div>
        </div>
    </div>
</body>
</html>