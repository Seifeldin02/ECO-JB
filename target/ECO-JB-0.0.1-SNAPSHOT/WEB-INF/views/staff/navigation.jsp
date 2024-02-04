<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="sidebar">
    <button id="toggle-sidebar"></button>
    <div class="logo">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
    </div>
    <ul class="menu">
        <li class="profile"><a class="nav-link" href='<c:url value="/staff/profile"/>'>Profile</a></li>
        <li class="calendar"><a class="nav-link" href='<c:url value="/staff/calendar"/>'>Calendar</a></li>
        <li class="dashboard">
            <a class="nav-link" href='<c:url value="/staff/dashboard"/>'>Dashboard</a>
            <ul class="submenu">
                <li><a href='<c:url value="/staff/carbon"/>'>Carbon Analysis</a></li>
                <li><a href='<c:url value="/staff/details"/>'>My Details</a></li>
                <li><a href='<c:url value="/staff/applications"/>'>User Applications!</a></li>
            </ul>
        </li>
        <li class="logout"><a class="nav-link" href='<c:url value="/staff/logout"/>'>Sign Out</a></li>
    </ul>
<script><%@include file="/js/navigation.js"%></script>
</div>