package com.ino.myblog.controller.api;

import com.ino.myblog.config.auth.PrincipalDetail;
import com.ino.myblog.dto.ResponseDto;
import com.ino.myblog.model.RoleType;
import com.ino.myblog.model.User;
import com.ino.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;

import static com.ino.myblog.util.SHA256.sha256;

@RestController
public class UserApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){ //username, password, email
        System.out.println("UserAPiController : save 호출");
        String emailHash=sha256(user.getEmail());
        user.setEmailHash(emailHash);
        userService.save(user);
        userService.sendVerificationEmail(user.getEmail(),emailHash);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);//자바 오브젝트를 json으로 변환해서 리턴
    }
    @PutMapping("/user")
    public ResponseDto<Integer> update(@RequestBody User user){  //<-json if requsetbody x -> key=value, x-www-form-urlencode
        userService.userUpdate(user);
        //userService에서 userUpdate가 종료되면 트랜잭션이 종료되기 때문에 Db에 값은 변경이 됐지만
        //session 값은 변경되지 않은 상태이기 때문에 직접 세션값을 변경해야함
        //session 등록
        Authentication authencation = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authencation);

        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

}
