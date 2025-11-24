package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 요청/응답 제어 역할 명시  + bean 등록
public class ExampleController {
	
	// 1) @RequestMapping("주소")
	
	// 2) @GetMapping("주소")	: get(조회) 방식 요청
	//	  @PostMapping("주소")	: post(삽입) 방식 요청
	//	  @PutMapping("주소")	: put(수정) 방식 요청 (form,a 태그 요청 불가)
	//	  @DeleteMapping("주소"): delete(삭제) 방식 요청 (form,a 태그 요청 불가)
	
	
	@GetMapping("example")
	public String exampleMethod() {
		
		// forward하여는 html 파일 경로 return 작성
		// 단, viewpesolver가 제공하는
		// 타임리프의 접두사, 접미사는 제외하고 작성
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		return"example";
		// srx/main/resources/templates/example.html
	}
	
}
