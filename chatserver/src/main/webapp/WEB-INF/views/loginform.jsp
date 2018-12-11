<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
        <link href="./css/style.css?ver=1" rel="stylesheet">
        <title>Login</title>
    </head>
    <body>
        <div class="login_form">
            <form action="./auth" method="POST">
                <div>
                    <label>ID : </label><input type="text" name="id">
                </div>
                <div>
                    <label>PW : </label><input type="password" name="password">
                </div>
                <input type="submit">
            </form>
        </div>
    </body>
</html>