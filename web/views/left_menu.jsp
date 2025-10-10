<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="sidebar">
    <ul class="menu-list">
        <li><a href="/profile" class="menu-item active"><i class="fa fa-user"></i> Profile</a></li>
        <li><a href="#"><i class="fa fa-bell"></i> Notifications</a></li>
        <li><a href="#"><i class="fa fa-briefcase"></i> Trip History</a></li>
        <li><a href="#"><i class="fa fa-users"></i> Story</a></li>
        <li><a href="#"><i class="fa fa-fingerprint"></i> Security</a></li>
    </ul>
</div>

<style>
.sidebar {
    width: 220px;
    background-color: #f8f9fa;
    padding: 20px 0;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    height: 100%;
}
.menu-list {
    list-style: none;
    padding: 0;
    margin: 0;
}
.menu-item {
    display: flex;
    align-items: center;
    color: #333;
    padding: 10px 20px;
    text-decoration: none;
    font-weight: 500;
    transition: 0.3s;
}
.menu-item i {
    margin-right: 10px;
    font-size: 18px;
}
.menu-item:hover, .menu-item.active {
    color: #007bff;
    background-color: #eaf2ff;
    border-right: 4px solid #007bff;
}
</style>

<!-- FontAwesome icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
