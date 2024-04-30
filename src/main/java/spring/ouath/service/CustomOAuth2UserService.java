package spring.ouath.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import spring.ouath.domain.UserEntity;
import spring.ouath.dto.*;
import spring.ouath.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 상위 클래스의 생성자를 받아와 userRequest 값을 넣어줌
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User: " + oAuth2User.toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }
        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디 값을 만듦
        String username = oAuth2Response.getName() + oAuth2Response.getProviderId();
        UserEntity existUser = userRepository.findByUsername(username);
        log.info(username);


        if(existUser == null) {
            log.info("~~~~~~~~~~~~~~~~~~~~~~첫 로그인!");
            UserEntity newUser = UserEntity.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(newUser);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();
            return new CustomOAuth2User(userDTO);
        } else{
            log.info("~~~~~~~~~~~~~~~~~~~~~~첫 로그인 아님!");
            existUser.updateEmailAndName(oAuth2Response.getEmail(), oAuth2Response.getName());
            userRepository.save(existUser);

            UserDTO userDTO = UserDTO.builder()
                    .username(existUser.getUsername())
                    .name(existUser.getName())
                    .role(existUser.getRole())
                    .build();
            return new CustomOAuth2User(userDTO);
        }



    }
}
