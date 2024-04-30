package spring.ouath.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;
/*
oauth 를 제공하는 service 마다 attribute 가 매우 상이하기 때문에 getAttributes는 사용하지 않고 custom해서 사용한다.
 */
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    public String getUsername(){
        return userDTO.getUsername();
    }

    // role return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return authorities;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }
}
