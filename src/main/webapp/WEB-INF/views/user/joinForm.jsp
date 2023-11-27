<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>

<div class="container">
    <form action="/user/join" >
        <div class="form-group">
            <label for="username">아이디:</label>
            <input type="text" class="form-control" placeholder="아이디 입력" id="username">
        </div>
        <div class="form-group">
            <label for="email">이메일:</label>
            <input type="email" class="form-control" placeholder="이메일 입력" id="email">
        </div>
        <div class="form-group">
            <label for="password">비밀번호:</label>
            <input type="password" class="form-control" placeholder="비밀번호 입력" id="password">
        </div>

    </form>
    <button id="btn-user-save" class="btn btn-primary">회원가입 완료</button>
</div>


<%@include file="../layout/footer.jsp"%>


