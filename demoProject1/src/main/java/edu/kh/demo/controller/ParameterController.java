package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("param") // /param으로 시작하는 요청을 현태 컨트롤러로 매핑
@Slf4j // log를 이용한 메시지 콘솔창에 출력할 때 사용 (lombok 제공)
public class ParameterController {

	@GetMapping("main")
	public String paramMain() { // /param/main Gat 방식 요청 매핑
		
		return "param/param-main";
	}
	
	/*
	 * HttpServletRequest
	 * - 요청 클라이안트의 정보, 제출된 파라미터 등을 저장하 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * Spring 의 Controller 단 멧더 작성시
	 * 매개변수에 원하는 개체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * --> ArgumentResolver (전달 인자 해결사)
	 * */
	
	@PostMapping("test1")
	public String paramTest1(HttpServletRequest req) {

		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt( req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		log.debug("inputName :" + inputName);
		log.debug("inputAge :" + inputAge);
		log.debug("inputAddress :" + inputAddress);
		
		/*
		 * Spring 에서 Redirect(재요청) 하는 방법
		 * -Controller 메서드 반환값에
		 * "redirect: 재요청 주소"; 작성
		 * 
		 * */
		return "redirect:/param/main";
	
	}
}
