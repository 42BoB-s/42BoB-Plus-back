package com.bobPlus.service;

import com.bobPlus.mapper.UserMapper;
import com.bobPlus.dto.SessionDto;
import com.bobPlus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${42OAuthUid}")
    String uid;
    @Value("${42OAuthSecret}")
    String secret;
    @Value("${baseUrl}")
    String baseUrl;

    public String getOAuthToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = null;
        String accessTokenUrl = "https://api.intra.42.fr/oauth/token"
                + "?grant_type=authorization_code"
                + "&client_id=" + uid
                + "&client_secret=" +secret
                + "&code=" + code
                + "&redirect_uri=" + baseUrl + "42OAuth"; // 42OAuth 는 API 발급할때 지정된 redirect URL
        response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);

        JSONParser parser = new JSONParser();
        String access_token = null;
        try{
            Object obj = parser.parse(response.getBody());
            JSONObject jsonObj = (JSONObject) obj;
            access_token = (String)jsonObj.get("access_token");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    public SessionDto getUserInfo(String token) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ token);
        headers.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = null;
        String getInfoUrl = "https://api.intra.42.fr/v2/me";
        response = restTemplate.exchange(getInfoUrl, HttpMethod.GET, request, String.class);
        SessionDto sessionDto = new SessionDto();
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(response.getBody());
            JSONObject jsonObj = (JSONObject) obj;
            sessionDto.setUserId((String)jsonObj.get("login"));
            sessionDto.setEmail((String)jsonObj.get("email"));
            sessionDto.setProfile((String)jsonObj.get("image_url")); // https://api.intra.42.fr/apidoc/2.0/users/me.html 를 참조하여 get(image_url)
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sessionDto;
    }

    // userId 가 DB 에 없다면 user 정보를 등록
    public void processNewUser(SessionDto sessionDto) {
        // 해당 user 정보가 없다면 sessionDto 정보로 user 생성
        if (!userService.userIdCheck(sessionDto.getUserId()))
            userRepository.save(userMapper.convertToUser(sessionDto));
    }

    public HttpSession getSession(HttpServletRequest req, SessionDto sessionDto) {

        HttpSession session = req.getSession();

        if (session.getAttribute("session") == null)
            session.setAttribute("session", sessionDto);
        return session;
    }

    public void destroySession(HttpSession httpSession) {
        httpSession.invalidate();
    }
}

