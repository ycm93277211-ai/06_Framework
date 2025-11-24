package edu.kh.demo.controller;

import edu.kh.demo.DemoProject1Application;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// instance : 개발자가 직접 new 연산자를 통해 만든 객체, 관리하는 객체

// bean : Spring Container가 만들고, 관리하는 객체

// IOC (제어의 반전) : 객체의 생성 및 생명주기의 권한이 개발자가 아닌,
//					 프레임 워크에게 있다


@Controller //  요청/응답 을 제어하는 역할 + Bean 등록
//@RequestMapping("/test") // 서블릿이랑 똑같이 쓰면 됨 
public class TestController {

    private final DemoProject1Application demoProject1Application;

    TestController(DemoProject1Application demoProject1Application) {
        this.demoProject1Application = demoProject1Application;
    }

	// 기존 Servlet :  클래스 단위로 하나의 요청만 처리 가능
	// Spring : 메서드 단위로 요청 처리 가능(여러개 가능)
	
	//@RequestMapping("요청주소")
	// - 요청 주소를 처리할 클래스 or 메서드를 매핑하는 어노테이션
	
	// 1) 클래스와 메서드에 함께 작성  
	// - 공통 주소를 매핑
	// ex) /test/insert , /test/update ... 
	
//	@RequestMapping("/insert")
//	public void methodA() {}
	// test/insert 요청을 methodA가 처리하겠다.
	
//	@RequestMapping("/update")
//	public void methodB() {}
	// test/update 요청을 methodB가 처리하겠다.
	
	
	// 2) 메서드에 작성
	// - 요청 주소와 해당 메서드를 매핑
	// - GET/POST 가리지 않고 매핑
	// (속성을 통에서 지정 가능 or 다른 어노테이션 이용
	
	
//	@RequestMapping(value="/test",method = RequestMethod.GET ) 
	@RequestMapping("/test") // /test 요청 시 testMethod가 매핑하여 처리함
	public String testMethod() {
		
		System.out.print("/test 요청받음");
		
		/*
		 * Controller 메서의 반환형이 String인 이유
		 * -> 메서드에서 반환되는 문자열이
		 * 	forward 할 html 파일의 경로가 되기 때문
		 *
		 * Thymeleaf : JSP 대신 사용하는 템플릿 엔진(html형태)
		 * 
		 * 접두사 : classpath:/templates/
		 * 접미사 : .html
		 * */
		
		
		return"test";
		
	}
	
	
}
