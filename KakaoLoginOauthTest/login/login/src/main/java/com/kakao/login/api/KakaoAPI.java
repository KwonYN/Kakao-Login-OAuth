package com.kakao.login.api;

import com.kakao.login.config.APIInfoConfig;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RestController
public class KakaoAPI {

    public String getAccessToken(String code) {
        System.out.println("getAccessToken");

        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";   // 토큰 받기, 토큰 갱신하기 (POST)

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("conn : " + conn);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=025d1f265573b2bbd67646c96831e668");  // 025d1f265573b2bbd67646c96831e668
            sb.append("&redirect_uri=http://localhost:8080/login");    // http://localhost:8080/login
            sb.append("&code=" + code);

            bw.write(sb.toString());    // sb.toString() : grant_type=authorization_code&client_id=025d1f265573b2bbd67646c96831e668&redirect_uri=http://localhost:8080/login&code=N5FlqCSE0nM3ogA3VouNDLW9sLbWoPC5aVr0hDWZWJjq7cQPq7C7ui4Ql0Wux59tBJQRjwopcNEAAAF6hofmtA
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("response code : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            System.out.println("accessToken : " + accessToken);
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
            System.out.println("refreshToken : " + refreshToken);

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        System.out.println("getUserInfo");

        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";    // 사용자 정보 가져오기 (GET, POST)

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("response code : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public void kakaoLogout(String accessToken) {
        System.out.println("kakaoLogout");

        String reqURL = "https://kapi.kakao.com/v1/user/logout";    // 로그아웃 (POST)
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer "+accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("response code : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
