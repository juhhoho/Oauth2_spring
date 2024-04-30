package spring.ouath.dto;

import java.util.Map;

/*
네이버의 인증 서버는 다음과 같은 방식으로 response 를 반환함
{resultcode=00, message=success, response={id=123123123, name=개발자유미, ...}}
 */
public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attributes;
    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
