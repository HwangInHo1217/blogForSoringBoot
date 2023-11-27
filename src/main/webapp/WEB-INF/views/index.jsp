<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>


<%@include file="layout/header.jsp"%>



<div class="container">
    <div class="posts-container ">
    <c:forEach var="board" items="${boards.content}">
        <div class="card m-2">
            <div class="card-body">
                <h5>글쓴이 : <a href="/user/${board.user.id}"> ${board.user.username}</a></h5>
                <h4 class="card-title">${board.title}</h4>
                <p>좋아요 : ${board.likeCount} 댓글 : ${board.replyCount}</p>
                <a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
            </div>
        </div>
    </c:forEach>

    <ul class="pagination justify-content-center">
        <c:choose>
            <c:when test="${boards.first}">
                <li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item "><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
            </c:otherwise>

        </c:choose>

        <c:choose>
            <c:when test="${boards.last}">
                <li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
            </c:otherwise>
        </c:choose>

    </ul>
        <br>
    <form action="/?searchKeyword{searchKeyword}" method="GET">
        <div class="input-group mt-3 mb-3">
            <button type="button" class="btn btn-outline-secondary dropdown-toggle" data-toggle="dropdown">
                필터
            </button>
            <div class="dropdown-menu">
                <a class="dropdown-item"  data-filter="post">게시물</a>
                <a class="dropdown-item"  data-filter="user">사용자</a>
            </div>
            <input type="hidden" name="filterType" id="filterType" value="post">
            <input type="text" name="searchKeyword" class="form-control" placeholder="왼쪽 필터로 구분 기본값은 게시물 검색 입니다."/>
            <button id="btn-search" type="submit" class="btn btn-primary">검색</button>
        </div>

    </form>
    </div>

</div>

<script src="/js/board.js"></script>
<%@include file="layout/footer.jsp"%>



