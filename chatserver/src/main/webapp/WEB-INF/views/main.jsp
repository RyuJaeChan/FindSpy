<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <link href="./css/style.css?ver=1" rel="stylesheet">
    <title>Main page</title>
</head>

<body>
    Main page
    <c:if test="${user ne null}">
        <h2>${user.getName()}님 안녕하세요!</h2>
        <form action="./logout" method="POST">
            <input type="submit" value="logout">
        </form>
    </c:if>
</body>

</html>