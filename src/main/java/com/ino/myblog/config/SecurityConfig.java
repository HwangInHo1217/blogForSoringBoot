package com.ino.myblog.config;

import com.ino.myblog.config.auth.PrincioalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 1. 어노테이션 제거
@Configuration
@EnableWebSecurity
public class SecurityConfig  { // 2. extends 제거

 /*   @Autowired
    private PrincioalDetailService princioalDetailService;*/
    // 3. principalDetailService 제거

    // 4. AuthenticationManager 메서드 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean//IoC 가능
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }
    // 5. 기본 패스워드 체크가 BCryptPasswordEncoder 여서 설정 필요 없음.
    /*시큐리티가 대신 로그인 해주는데 password를 가로채기 하는데
     * 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
     * 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
     */

    // 6. 최신 버전(2.7)으로 시큐리티 필터 변경
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. csrf 토큰 비활성화 (테스트 시 걸어두는 게 좋음)
        http.csrf().disable();

        // 2. 인증 주소 설정
        http.authorizeRequests(
                authorize -> authorize.antMatchers("/", "/ws/**","/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        );

        // 3. 로그인 처리 프로세스 설정
        http.formLogin(f -> f.loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc")//스프링 시큐리티가 해당 주소로 오는 요청을 가로채서 대신 로그인
                .defaultSuccessUrl("/")
                .failureUrl("/error")
        );
        return http.build();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                // 다른 설정들을 추가할 수 있음
                .authorizeRequests()
                .antMatchers("/chat/**").authenticated() // 채팅 관련 URL은 인증이 필요
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        // 사용자 정보를 세션에 저장
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }
}
