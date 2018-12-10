<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<title>Game Room!</title>
	<link href="${pageContext.request.contextPath}/css/style.css?ver=1" rel="stylesheet">
</head>

<body>
	<div class="wrap">
		<div class="header_area">
			Head
		</div>
		<div class="padding">

		</div>
		<div class="chat_area">
			<table class="chat">
				<tbody>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
					<tr>
						<td class="thumbnail_area">
							<img class="thumbnail_img" src="./img/01.jpg" alt="">
						</td>
						<td class="text_area">
							<div class="name">
								<span>유재찬</span>
							</div>
							<div class="comment">
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
								안녕하세요 반가워요안녕하세요
								반가워요안녕하세요 반가워요
							</div>
						</td>
					</tr>
				</tbody>
			</table>

		</div>
		<div class="input_area">
			<input class="chat_input" type="text" name="값의 이름" value="값" />
			<button>Button</button>
		</div>
	</div>

	<script src="./js/sockjs.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
	<script src="./js/chat.js"></script>
</body>

</html>