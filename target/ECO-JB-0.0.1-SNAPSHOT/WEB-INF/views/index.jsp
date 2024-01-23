<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<style><%@include file="/styles/index.css"%></style>
<body>
    <div class="container">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
        <div class="title">IPRK</div>
        <div class="subtitle">ISKANDAR PUTERI RENDAH KARBON</div>
        <div class="header">Choose sign in option</div>
        <a href="<c:url value='/user/login'/>">
            <div class="button">I’m a user</div>
        </a>
        <a href="<c:url value='/staff/login'/>">
            <div class="button">I’m a staff</div>
        </a>
        <a href="<c:url value='/institution/login'/>">
            <div class="button">I’m an institution</div>
        </a>
    </div>
</body>
</html>