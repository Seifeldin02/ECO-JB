<div id="sidebar">
    <div class="logo">
        <img class="logo" src='<c:url value="/images/index.png"></c:url>' />    
    </div>
    <ul class="menu">
        <li class="profile"><a href="/user/profile">Profile</a></li>
        <li class="calendar"><a href="/user/calendar">Calendar</a></li>
        <li class="dashboard">
            <a href="/user/dashboard">Dashboard</a>
            <ul class="submenu">
                <li><a href="/user/carbon-analysis">Carbon Analysis</a></li>
                <li><a href="/user/my-details">My Details</a></li>
                <li><a href="/user/join-us">Join us!</a></li>
            </ul>
        </li>
        <li class="logout"><a href="/user/logout">Sign Out</a></li>
    </ul>
<script><%@include file="/js/navigation.js"%></script>
</div>