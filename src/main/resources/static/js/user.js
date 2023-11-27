let index = {
    init:function (){
        $("#btn-user-save").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.save();
        });
        $("#btn-user-update").on("click", ()=>{ // function(){} , ()=>{} this를 바인딩하기 위해서!!
            this.update();
        });
        this.checkEmailStatus();
    },
    save:function () {
        alert("이메일 인증까지 다소 시간이 걸리는 점 양해 부탁드립니다."+'\n'+"현재 경고창은 닫으신 후 회원가입 완료창이 나올 때 까지 잠시만 대기 부탁드립니다.");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };
        console.log(data);
        //ajax 호출 시 default 가 비동기 호출
        //ajax가 통신을 성공하고, 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환

        if (!data.username || !data.password || !data.email) {
            alert("입력되지 않은 항목이 있습니다.");
            return; // 함수 실행 중단
        }
        $.ajax({
            //회원가입 수행 요청
            //요청 정상수행 done
            //실패 시 fail
            type:"POST",
            url:"/auth/joinProc",
            data:JSON.stringify(data),//http body data
            contentType:"application/json; charset=utf-8",//bodt데이터가 어떤 타입인지(MIME)
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            console.log(resp);
            if(resp.status == 500) {
                alert("회원가입 실패 : 이미 존재하는 아이디 입니다.");
            }
            else if (resp.status == 200) { // 회원가입 성공
                alert("회원가입 완료");
                alert("이메일 인증 안내를 확인해 주세요.");
                location.href="/";
            }
            else{
                alert("회원가입 처리 중 오류가 발생했습니다.")
            }
            //alert(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    update:function () {
        //alert("버튼 클릭");
        let data = {
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val(),
            bio: $("#bio").val()
        };

        $.ajax({
            type:"PUT",
            url:"/user",
            data:JSON.stringify(data),//http body data
            contentType:"application/json; charset=utf-8",//bodt데이터가 어떤 타입인지(MIME)
            dataType:"json"//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로 오기 때문에 문자열로 옴( 생긴게 json이라면) => javasciript 오브젝트로 변경해줌
        }).done(function(resp){
            alert("회원수정 완료");
            //alert(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!
    },
    checkEmailStatus: function() {
        $.ajax({
            type: "GET",
            url: "/user/checkEmailStatus",
            dataType: "text"
        }).done(function(resp){
            if(resp == 'NO' || resp.status == 500) {
                alert("이메일 인증이 완료되지 않았습니다. 이메일을 확인해주세요.");
                location.href="/logout"
            }
        }).fail(function(error){
            console.log(error);
        });
}
}
index.init();

/*
* 회원가입시 AJAX를 사용하는 2가지 이유
* 요청에 대한 응답을 HTNL이 아닌 DATA(JSON)으로 받기 위해->서버를 2개가 아닌 1개만 사용 가능
* 비동기 통신을 하기 위해 -> 비절차적
* */