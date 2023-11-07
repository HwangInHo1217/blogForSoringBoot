<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>
<div class="container">
    <form action="/auth/loginProc" method="post">
        <div class="form-group">
            <label for="username">아이디:</label>
            <input type="text" name="username"class="form-control" placeholder="아이디 입력" id="username">
        </div>

        <div class="form-group">
            <label for="password">비밀번호:</label>
            <input type="password"  name="password" class="form-control" placeholder="비밀번호 입력" id="password">
        </div>

        <button id="btn-login" class="btn btn-primary">로그인</button>
        <a href="https://kauth.kakao.com/oauth/authorize?client_id=4e832c29b1a13a2c3bc9699fde40040f&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code">
            <img height="38" src="/image/kakao_login_button.png" />
        </a>
    </form>

</div>
<%@include file="../layout/footer.jsp"%>


