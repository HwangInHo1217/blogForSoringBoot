package com.ino.myblog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ino.myblog.config.auth.PrincipalDetail;
import com.ino.myblog.model.EmailCheck;
import com.ino.myblog.model.KakaoProfile;
import com.ino.myblog.model.OauthToken;
import com.ino.myblog.model.User;
import com.ino.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

//인증이 안된 사용자들이 출입 할 수 있는 경로 /auth
// 주소가 / 이면 index.jsp 허용
// static이하에 있는 /js/**, .css .i
@Controller
public class UserController {
    @Value("${ino.key}")
    private String inoKey;

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }
    @GetMapping("/auth/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token) {
        boolean isVerified = userService.verifyUser(token);
        if (isVerified) {
            return ResponseEntity.ok("인증 완료!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패.");
        }
    }
    @GetMapping("/user/checkEmailStatus")
    public ResponseEntity<?> checkEmailStatus(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        String emailCheckStatus = principalDetail.getEmailCheck();
        return ResponseEntity.ok(emailCheckStatus);
    }
    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return  "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(){return "user/updateForm";}

/*    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallback(String code){ //Data를 리턴해주는 컨트롤러 함수

        //post방식으로 key=value 데이터 요청 ( 카톡 쪽으로)
        RestTemplate rt = new RestTemplate();
        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");


        //Httpbody 오브젝트 생성
        MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","4e832c29b1a13a2c3bc9699fde40040f");
        params.add("redirect_uri","http://localhost:8000/auth/kakao/callback");
        params.add("code",code);

        //헤더와 바디를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
            new HttpEntity<>(params,headers);
        //http 요청하기 -post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper=new ObjectMapper();
        OauthToken oauthToken=null;
        try {
            oauthToken=objectMapper.readValue(response.getBody(),OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("kakao_oauthToken = " + oauthToken.getAccess_token());
        //post방식으로 key=value 데이터 요청 ( 카톡 쪽으로)
        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        //헤더와 바디를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);
        //http 요청하기 -post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        ObjectMapper objectMapper2=new ObjectMapper();
        KakaoProfile kakaoProfile =null;
        try {
            kakaoProfile=objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("kakaoProfile = " + kakaoProfile.getId());
        System.out.println("kakaoUserbme =" + kakaoProfile.getId()+"_of_KaKao");
     //   UUID garbagePassword=UUID.randomUUID();//UUID -> 중복되지 않는 특정 값을 만드는 알고리즘
        System.out.println("inoKey = " + inoKey);

        User kakaoUser= User.builder()
                .username(kakaoProfile.getId()+"_of_KaKao")
                .password(inoKey.toString())
                .email("of_kakao")
                .emailCheck(EmailCheck.YES)
                .oauth("kakao")
                .build();

        User originUser=userService.findUser(kakaoUser.getUsername());
        if(originUser.getUsername()==null){ //기존 회원이 아닐경우 kakao 정보로 회원가입
            System.out.println("-----------------기존회원아님--------------------");
            userService.save(kakaoUser);
        }


        Authentication authencation = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),inoKey));
        SecurityContextHolder.getContext().setAuthentication(authencation);

        return "redirect:/";

    }*/
    @GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수

		// POST방식으로 key=value 데이터를 요청 (카카오쪽으로)
		// Retrofit2
		// OkHttp
		// RestTemplate

		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
       MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","4e832c29b1a13a2c3bc9699fde40040f");
        params.add("redirect_uri","http://localhost:8000/auth/kakao/callback");
        params.add("code",code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);

		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OauthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());

		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);
		System.out.println(response2.getBody());

		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}


		// UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		System.out.println("블로그서버 패스워드 : "+inoKey);

		User kakaoUser = User.builder()
                .username(kakaoProfile.getId()+"_of_KaKao")
				.password(inoKey)
                .email("of_kakao")
                .emailCheck(EmailCheck.YES)
				.oauth("kakao")
				.build();

		// 가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.findUser(kakaoUser.getUsername());

		if(originUser.getUsername() == null) {
			System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다");
			userService.save(kakaoUser);
		}

		System.out.println("자동 로그인을 진행합니다.");
		// 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), inoKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/";
	}
}


