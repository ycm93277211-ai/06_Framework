package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청 응답 제어 역할 명시 + bean 등록
@Slf4j // 로그객체 자동 생성
public class MainController {
	
	@Autowired // 타입이 같거나 상속관계를 DI 함(자동으로 객체 만들어줌)
//	private TodoService service = new TodoService;
	private TodoService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		
		log.debug("service : " + service);
		//edu.kh.todo.model.service.TotoServiceImpl@3d2aee95 객체화 잘 됨!
		
		// todoNo 가 1인 todo의 제목 조회하여 request scope 추가
		String testTitle = service.testTitle();
		
		model.addAttribute("testTitle",testTitle);
		
		// ------------------------------------
		// TB_TODO 테이블에 저장된 전체 할 일 목록 조회
		// + 완료된 할 일 갯수
		
		// service 메서드 호출 후 결과 반환 받기
		
		Map<String, Object> map = service.selectAll();
		
		// map에 담긴 내용 추출
		List<Todo> todoList = (List<Todo>)map.get("todoList"); 
		int completeCount =	(int)map.get("completeCount"); 
		
		// Model을 이용해서 조회 결과들을 result scope에 추가
		model.addAttribute("todoList",todoList);
		model.addAttribute("completeCount",completeCount);
		
		return "common/main";
	}
	
	
}
