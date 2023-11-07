<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>

<div class="container">
    <form action="/user/join" >
        <input type="hidden" id="id"value="${principal.user.id}"/>
        <div class="form-group">
            <label for="username">아이디:</label>
            <input type="text"  value="${principal.user.username}" class="form-control" placeholder="Enter username" id="username" readonly>
        </div>
        <c:if test="${empty principal.user.oauth}">
            <div class="form-group">
                <label for="password">비밀번호:</label>
                <input type="password" class="form-control" placeholder="비밀번호 입력" id="password">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" value="${principal.user.email}"class="form-control" placeholder="이메일 입력" id="email" readonly>
            </div>
        </c:if>

    </form>
    <button id="btn-update" class="btn btn-primary">정보수정 완료</button>
</div>


<%@include file="../layout/footer.jsp"%>


