<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <link href="./css/style.css?ver=1" rel="stylesheet">
    <title>추리추리 마추리</title>
</head>

<body>
    <div class="conA">
        <c:if test="${user ne null}">
            <div class="logout">
                <form action="./logout" method="POST">
                    <input class="button" type="submit" value="LOGOUT">
                </form>
            </div>
        </c:if>
        <div class="container">
            <h1>추리추리 마추리</h1>
            <p>범인은 누구?</p>
            <c:choose>
                <c:when test="${user ne null}">
                    <a href="./games">게임시작</a>
                </c:when>
                <c:otherwise>
                    <a href="./loginform">로그인하기</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>

</html>