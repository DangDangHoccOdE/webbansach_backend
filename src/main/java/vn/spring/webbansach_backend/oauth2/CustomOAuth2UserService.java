package vn.spring.webbansach_backend.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.spring.webbansach_backend.entity.AuthProvider;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.exception.OAuth2AuthenticationProcessingException;
import vn.spring.webbansach_backend.oauth2.user.OAuth2UserInfo;
import vn.spring.webbansach_backend.oauth2.user.OAuth2UserInfoFactory;
import vn.spring.webbansach_backend.service.inter.IUserService;

import java.util.*;

//Lớp CustomOAuth2UserService chịu trách nhiệm:
//Xử lý thông tin người dùng từ OAuth2 provider.
//Tìm kiếm hoặc đăng ký người dùng mới trong hệ thống.
//Cập nhật thông tin người dùng đã tồn tại.
//Đảm bảo rằng người dùng đang đăng nhập bằng cùng một provider mà họ đã sử dụng để đăng ký.
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final IUserService iUserService;

    @Autowired
    public CustomOAuth2UserService(@Lazy IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email không tìm thấy trong provider");
        }

        User userOptional = iUserService.findUserByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional != null) {
            user = userOptional;
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Có vẻ như bạn đã đăng ký với " +
                        user.getProvider() + " tài khoản. Vui lòng sử dụng " + user.getProvider() +
                        " tài khoản để đăng nhập.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        if (iUserService.findUserByEmail(oAuth2UserInfo.getEmail()) != null) {
            throw new OAuth2AuthenticationProcessingException("Tài khoản google đã được dùng để đăng ký cho tài khoản khác!");
        }

        User user = new User();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getGivenName());
        user.setLastName(oAuth2UserInfo.getFamilyName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setAvatar(oAuth2UserInfo.getImageUrl());
        user.setActive(true);

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return iUserService.saveUserWithRole(user,roles);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getGivenName());
        existingUser.setLastName(oAuth2UserInfo.getFamilyName());
        return iUserService.saveUser(existingUser);
    }

}
