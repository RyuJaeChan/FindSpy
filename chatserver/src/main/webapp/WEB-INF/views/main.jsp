<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <link href="./css/style.css?ver=1" rel="stylesheet">
    <title>Main page</title>
</head>

<body>
    <div class="wrap">
    Main page
    <c:choose>
        <c:when test="${user ne null}">
            <h2>${user.getName()}님 안녕하세요!</h2>
            <form action="./logout" method="POST">
                <input type="submit" value="logout">
            </form>
            <a href="./games">Game Room 목록</a>
        </c:when>
        <c:otherwise>
            <a href="./loginform">Login</a>
        </c:otherwise>
    </c:choose>
    </div>
</body>

</html>