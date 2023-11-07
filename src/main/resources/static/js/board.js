let index = {
    init:function (){
        $("#btn-save").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.save();
        });
        $("#btn-delete").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.deleteById();
        });
        $("#btn-update").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.update();
        });
        $("#btn-reply-save").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.replySave();
        });
        $("#btn-search").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.searchBoard();
        });
    },
    save:function () {
        //alert("버튼 클릭");
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        $.ajax({

            type:"POST",
            url:"/api/board",
            data:JSON.stringify(data),
            contentType:"application/json; charset=utf-8",//bodt데이터가 어떤 타입인지(MIME)
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            alert("글쓰기 완료");
            //alert(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    deleteById:function () {
        let id = $("#id").text();
        $.ajax({
            type:"DELETE",
            url:"/api/board/"+id,
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            alert("삭제 완료");
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    update:function () {
        let id=$("#id").val();

        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };
        console.log(id);
        console.log(data);
        $.ajax({
            type:"PUT",
            url:"/api/board/"+id,
            data:JSON.stringify(data),
            contentType:"application/json; charset=utf-8",//bodt데이터가 어떤 타입인지(MIME)
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            alert("글수정 완료");
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    replySave:function () {
        //alert("버튼 클릭");
        let data = {
            userId:$("#userId").val(),
            boardId:$("#boardId").val(),
            content: $("#reply-content").val()
        };
        let boardId = $("#boardId").val();
       $.ajax({

            type:"POST",
            url:`/api/board/${data.boardId}/reply`,
            data:JSON.stringify(data),
            contentType:"application/json; charset=utf-8",//bodt데이터가 어떤 타입인지(MIME)
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            alert("댓글 작성 완료");
            //alert(resp);
            location.href=`/board/${data.boardId}`;
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    replyDelete : function(boardId, replyId){
        $.ajax({
            type: "DELETE",
            url: `/api/board/${boardId}/reply/${replyId}`,
            dataType: "json"
        }).done(function(resp){
            alert("댓글삭제 완료");
            location.href = `/board/${boardId}`;
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    searchBoard : function (){
        let data={
            title :$("#title").val(),
            contente : $("#content").val()
        };

        $.ajax({
            type : "GET",
            url:"/api"
        })

    }

}
index.init();

/*
* 회원가입시 AJAX를 사용하는 2가지 이유
* 요청에 대한 응답을 HTNL이 아닌 DATA(JSON)으로 받기 위해->서버를 2개가 아닌 1개만 사용 가능
* 비동기 통신을 하기 위해 -> 비절차적
* */