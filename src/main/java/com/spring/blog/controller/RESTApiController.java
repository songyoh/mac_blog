package com.spring.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Controller // 컨트롤러로 지정
//@ResponseBody // REST형식 전환, 메서드 위에 붙으면 해당 메서드만 REST형식
@RestController // Controller와 ResponseBody를 한 번에 지정해줌
@RequestMapping("/resttest")
public class RESTApiController { // url을 분석

    // REST 컨트롤러는 크게 json을 리턴하거나, String을 리턴하게 만들 수 있다.
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "안녕하세요";
    }

    // 문자 배열도 리턴 가능하다.
    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public List<String> foods(){
        List<String> foodList = List.of("탕수육", "똠얌꿍", "돈카츠");
        return foodList;
    }
}
