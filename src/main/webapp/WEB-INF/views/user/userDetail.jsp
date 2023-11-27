<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>
<style>
    .profile-header {
        margin-top: 20px;
    }
    .profile-img {
        width: 150px;
        height: 150px;
        border-radius: 50%;
    }


    .category-delete-btn {
        float: right;
        color: #dc3545;
        cursor: pointer;
    }
</style>

<!--프로필 섹션-->
<div class="profile">
    <!--유저정보 컨테이너-->
    <div class="Container">

        <!--유저정보 및 사진등록 구독하기-->
        <div class="profile-header text-center">
            <form id="userProfileImageForm">
                <input type="file" name="profileImageFile" style="display: none;"
                       id="userProfileImageInput" />
            </form>
            <img class="profile-img" src="/upload/${dto.user.profileImageUrl}"
                 id="userProfileImage" />
            <h2>${dto.user.username}</h2>
            <c:choose>
                <c:when test="${empty dto.user.bio}">
                    <p class="lead">회원정보 수정 창에서 블로그 소개를 작성해주세요!</p>
                </c:when>
                <c:otherwise>
                    <p class="lead">${dto.user.bio}</p>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${dto.pageOwnerState}">
                    <button class="btn btn-primary" onclick="profileImageUpload(${dto.user.id}, ${principal.user.id})">사진 등록</button>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${dto.subscribeState}">
                            <button class="btn btn-primary" onclick="toggleSubscribe(${dto.user.id}, this)">구독취소</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-primary" onclick="toggleSubscribe(${dto.user.id}, this)">구독하기</button>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
            <button class="btn btn-primary" onclick="openModal(); subscribeInfoModalOpen(${dto.user.id});">구독 정보</button>
            <div class="modal-fade" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header"> 구독정보
                            <button type="button" class="close" onclick="closeModal()">&times;</button>
                        </div>
                        <div class="modal-body" id="subscribeModalList">
                            <!-- 구독 정보 내용이 여기에 동적으로 삽입됩니다 -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
                <h3>카테고리</h3>
            <div class="card">
                <ul class="list-group list-group-flush">
                    <%-- 서버로부터 받아온 카테고리 목록을 반복하여 표시 --%>
                    <c:forEach var="category" items="${dto.categories}">
                        <li class="list-group-item">
                            <a href="/user/${userId}/category/${category.id}">${category.name}</a>
                            <c:if test="${dto.pageOwnerState}">
                                <span class="category-delete-btn" onclick="deleteCategory(${category.id})">&times;</span>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
                <%-- 로그인한 사용자가 프로필의 주인인 경우에만 추가 버튼 표시 --%>
                <c:if test="${dto.pageOwnerState}">
                    <div class="card-body">
                        <button class="btn btn-primary" onclick="addCategory()">카테고리 추가</button>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="col-md-9">
            <h3>게시물 목록</h3>
            <!-- 게시물 리스트 시작 -->
            <c:forEach var="board" items="${dto.boards.content}">
                <div class="card">
                    <div class="card-body">
                        <a href="/board/${board.id}"><h5>${board.title}</h5></a>
                        <div>
                            <span class="mr-2">좋아요: <i class="fas fa-heart"></i> ${board.likeCount}</span>
                            <span>댓글: ${board.replyCount}</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <ul class="pagination justify-content-center">
                <c:choose>
                    <c:when test="${dto.boards.first}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${dto.boards.number-1}">Previous</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${dto.boards.number-1}">Previous</a></li>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${dto.boards.last}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${dto.boards.number+1}">Next</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${dto.boards.number+1}">Next</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>

<script src="/js/profile.js"></script>
<script src="/js/category.js"></script>

<%@include file="../layout/footer.jsp"%>
