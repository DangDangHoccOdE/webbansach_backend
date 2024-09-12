package vn.spring.webbansach_backend.oauth2.user;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getGivenName();
    public abstract String getFamilyName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
