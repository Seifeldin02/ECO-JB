<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <style><%@include file="/styles/index.css"%></style>
</head>
<body>
    <div class="container">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
        <div class="title">IPRK</div>
        <div class="subtitle">ISKANDAR PUTERI RENDAH KARBON</div>
        <div class="header">Choose sign in option</div>
        <a class="button" href="<c:url value='/user/login'/>">
            <span>I’m a user</span>
        </a>
        <a class="button" href="<c:url value='/staff/login'/>">
            <span>I’m a staff</span>
        </a>
        <a class="button" href="<c:url value='/institution/login'/>">
            <span>I’m an institution</span>
        </a>
    </div>
</body>
</html>