package com.kakao.login.controller;

import com.kakao.login.api.KakaoAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
public class HomeController {

    private KakaoAPI kakaoAPI = new KakaoAPI();

    @RequestMapping(value = "/login")
    public ModelAndView login(@RequestParam("code") String code, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        // 1. 인증 코드 요청 전달 → 토큰 받기
        String accessToken = kakaoAPI.getAccessToken(code);
        // 2. 인증코드로 토큰 전달 → 토큰으로 자원 서버로부터 사용자 정보 받기
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(accessToken);

        System.out.println("login info : "+userInfo.toString());

        if(userInfo.get("email") != null) {
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("accessToken", accessToken);
        }
        mav.addObject("userId", userInfo.get("email"));
        mav.addObject("accessToken", accessToken);
        mav.setViewName("index");

        return mav;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        kakaoAPI.kakaoLogout((String) session.getAttribute("accessToken"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userId");
        mav.setViewName("index");

        return mav;
    }
}
