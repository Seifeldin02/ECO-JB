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
        <div class="user-type">Register</div>
        <div class="login-box">
            <div class="signin-header">Sign Up</div>
            <form action="<c:url value='/staff/register'/>" method="post">
                <div class="form-group">
                    <label for="name" class="input-label">Full Name:</label>
                    <input type="text" class="form-control input-field" id="name" placeholder="Enter full name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="hpNo" class="input-label">HP No:</label>
                    <input type="text" class="form-control input-field" id="hpNo" placeholder="Enter HP No" name="hpNo" required>
                </div>
                <div class="form-group">
                    <label for="address" class="input-label">Address:</label>
                    <input type="text" class="form-control input-field" id="address" placeholder="Enter address" name="address" required>
                </div>
                <div class="form-group">
    <label for="dob" class="input-label">Date of Birth:</label>
    <input type="date" class="form-control input-field" id="dob" name="dob" required>
</div>
                <div class="form-group">
                    <label for="email" class="input-label">Email:</label>
                    <input type="email" class="form-control input-field" id="email" placeholder="Enter email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="password" class="input-label">Password:</label>
                    <input type="password" class="form-control input-field" id="password" placeholder="Enter password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="input-label">Confirm Password:</label>
                    <input type="password" class="form-control input-field" id="confirmPassword" placeholder="Confirm password" name="confirmPassword" required>
                </div>
                <button type="submit" class="button submit-button">Register</button>
            </form>
            <div class="link-container">
                <a href="<c:url value='/staff/login'/>" class="small-link">
                    Already a staff? Sign in here!
                </a>
                <c:if test="${not empty error}">
    <div class="error"><small>${error}</small></div>
</c:if>
            </div>
        </div>
    </div>
</body>
</html>