package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("email")
@RequiredArgsConstructor // final 필드에 자동을 의존성 주입
public class EmailController {

	// [핵심 1] 변수 앞에 'final'을 붙여야 @RequiredArgsConstructor가 생성자를 만들어 주입합니다.
    // final이 없으면 스프링이 이 필드를 무시하여 여전히 null 상태가 됩니다.
	private final EmailService service;

	@ResponseBody
	@PostMapping("signup")
	public int signup(@RequestBody Map<String, String> map) {
		
		String email = map.get("email");
		
		String authKey = service.sendEmail("signup", email);
		
		if(authKey != null) { // 인증번호가 반환되어 돌아옴 ==  이메일 보내기 성공!
			return 1;
		}
		// 이메일 보내기 실패
		return 0;
	}
	
	/** 입력받은 이메일, 인증번호가 DB에 있는지 조회 
	 * @param email, authKey
	 * @return
	 */
	@ResponseBody
	@PostMapping("checkAuthKey")
	public int checkAuthKey(@RequestBody Map<String, String> map) {
		
		return service.checkAuthKey(map);
		
	}
	

}
