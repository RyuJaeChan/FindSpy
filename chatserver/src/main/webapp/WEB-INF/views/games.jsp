<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<link href="./css/style.css?ver=1" rel="stylesheet">
	<title>Game Rooms</title>
</head>

<body>
	<div class="wrap">
		<div class="header_area">
			<span>Game Room List</span>
			<form class="button" action="./games" method="POST">
				<input type="submit" value="방생성">
			</form>
		</div>
		<div class="gamelist_area">
			<c:if test="${list.size()} eq 0">
				<p>참가 가능한 방이 없습니다.</p>
			</c:if>
			<ul>
				<c:forEach items="${list}" var="item">
					<a href="./gameroom/${item.getId()}">
						<li>user_name1님의 게임방</li>
					</a>
				</c:forEach>
			</ul>
		</div>

	</div>
</body>

</html>