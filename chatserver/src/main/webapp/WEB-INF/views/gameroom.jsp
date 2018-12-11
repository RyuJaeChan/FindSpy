<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<link href="${pageContext.request.contextPath}/css/style.css?ver=1" rel="stylesheet">
	<title>Game Room!</title>
</head>

<body>
		<div class="wrap">
			<input class="user_id" type="hidden" data-user_id="${userId}">
			<input class="roomid" type="hidden" data-roomid="${gameroom.getId()}">
			<input class="path" type="hidden" data-path="${pageContext.request.contextPath}">
				<div class="header_area">
					Welcome!
				</div>
		
				<div class="chat_area">
					
				</div>
		
				<div class="input_area">
					<input class="chat_input" type="text" name="값의 이름" value="값" />
					<button class="send_button">SEND</button>
				</div>
			</div>

	<script type="template" id="chat_template">
			<div class="chat_block">
				<div class="thumbnail_area">
					<img class="thumbnail_img" src="${pageContext.request.contextPath}/img/user.png">
				</div>
				<div class="text_area">
					<div class="name">
						<span>{writer}</span>
					</div>
					<div class="margin">
						<img class="arrow" src="${pageContext.request.contextPath}/img/left.png">
						</div>
						<div class="message">
							{message}
						</div>
					</div>
				</div>
	</script>
	<script type="template" id="mymessage">
			<div class="mymessage">
                    <div class="text_area">
                        <div class="message">
                           {message}
                        </div>
                        <div class="margin">
                            <img class="arrow" src="${pageContext.request.contextPath}/img/right.png">
                        </div>
                    </div>
                </div>
	</script>
	<script src="${pageContext.request.contextPath}/js/common/utils.js"></script>
	<script src="${pageContext.request.contextPath}/js/lib/sockjs.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
	<script src="${pageContext.request.contextPath}/js/socket.js"></script>
	<script src="${pageContext.request.contextPath}/js/gameroom.js"></script>
</body>

</html>