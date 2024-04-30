package spring.ouath.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import spring.ouath.jwt.JwtFilter;
import spring.ouath.jwt.JwtUtil;
import spring.ouath.oauth2.CustomSuccessHandler;
import spring.ouath.service.CustomOAuth2UserService;

import java.util.Collections;

@Configuration
@EnableWebSecurity //spring security 를 활성화하고 웹 보안을 구성하는 역할
@RequiredArgsConstructor
/*
csrf(cross site request forgery)란 사이트간 위조 요청을 말한다.
웹 보안 취약점의 일종이며 쿠키를 통해 공격하는데, jwt token 을 사용하는 경우 disable 할 수 있다.

jwt, oauth 를 사용하는 경우 csrf, formLogin, httpBasic 에 대해서는 고려할 필요없이 disable 하면 된다.
*/
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf disable
        http
                .csrf((auth) -> auth.disable());
        // form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());
        // HTTP basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        // oauth2
        /*
        아래의 ouath2에 관한 설정은 oauth2 로그인을 실행하고, 그 엔드포인트에 대한 설정을 진행하는 코드이다.

        http.oauth2Login() 는 oauth2를 인자로 받아서 OAuth2.0의 다양한 측면을 구성할 수 있다.
        이 때 userInfoEndpoint 는 사용자의 정보 엔드포인트에 대한 설정을 진행하는데, userInfoEndpointConfig 객체를 통해 사용자의 정보에 대해 설정을 진행한다.
        그리고 사용자의 정보를 설정하는 데에는 외부에서 주입받은 customOAuth2UserService 의 서비스 로직을 이용한다.
         */
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        // 경로별 인가 작업
        http
                .authorizeRequests((auth)->auth
                        .requestMatchers("/").permitAll() // root 경로에 대해서만 permit all
                        .anyRequest().authenticated());
        // 세션 설정 : STATELESS
        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // cors 설정
        http
                .cors(corsCustomizer-> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));


        return http.build();
    }
}
