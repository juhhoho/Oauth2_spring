package spring.ouath.dto;

import java.util.Map;


/*
{resultcode=00, message=success, id=123123123, name=개발자유미}
구글의 경우 네이버와 다르게 code, msg 외의 다른 값을 response 에 담지 않고 같이 직렬화해서 보낸다.
따라서 네이버처럼 response 의 값을 캐스팅해주지 않아도 된다.
 */
public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attributes;
    public GoogleResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
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
