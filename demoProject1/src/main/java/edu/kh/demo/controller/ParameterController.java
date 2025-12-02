package edu.kh.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.Meber;
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
	 * HttpServletRequest - 요청 클라이안트의 정보, 제출된 파라미터 등을 저장하 객체 - 클라이언트 요청 시 생성
	 * 
	 * Spring 의 Controller 단 멧더 작성시 매개변수에 원하는 개체를 작성하면 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * --> ArgumentResolver (전달 인자 해결사)
	 */

	@PostMapping("test1") // /param/test1 post 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) {

		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");

		log.debug("inputName :" + inputName);
		log.debug("inputAge :" + inputAge);
		log.debug("inputAddress :" + inputAddress);

		/*
		 * Spring 에서 Redirect(재요청) 하는 방법 -Controller 메서드 반환값에 "redirect: 재요청 주소"; 작성
		 * 
		 */
		return "redirect:/param/main";

	}

	// 2. @RequestParam 어노테이션 - 낱개 파라미터 얻어오기
	@PostMapping("test2") // /param/test2 post 방식 요청 매핑
	public String paramTest2(@RequestParam("title") String title, @RequestParam("writer") String writer,
			// int형 매개변수의 자동형변환이 사용된 경우에는
			// 파라미터값(value)이 필수로 작성되어야한다!
			@RequestParam("price") int price,
			@RequestParam(value = "publisher", required = false, defaultValue = "kh출판사") String publisher) {

		/*
		 * 2. @RequestParam 어노테이션 - 낱개 파라미터 얻어오기
		 *
		 * - request 객체를 이용한 파라미터 전달 어노테이션 - 매개변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨. -
		 * 주입되는 데이터는 매개변수의 타입에 맞게 형변환이 자동으로 수행됨.
		 *
		 * [기본 작성법]
		 * 
		 * @RequestParam("key") 자료형 매개변수명
		 *
		 * [속성 추가 작성법]
		 * 
		 * @RequestParam(value="key", required=false, defaultValue="1")
		 *
		 * value : 전달받은 input 태그의 name 속성값(파라미터 key) required : 입력된 name 속성값 파라미터 필수 여부
		 * 지정(기본값 true) -> required=true인 파라미터가 존재하지 않는다면 400(Bad Request) 에러 발생 -> ""
		 * (빈문자열)일 때는 에러 발생 X (파라미터가 존재하지 않는것이 아니라 name속성값="" 로 넘어오기 때문에)
		 *
		 * defaultValue : 파라미터 중 일치하는 name속성값이 없을 경우에 대입할 값 지정. -> required=false 인 경우
		 * 사용
		 *
		 *
		 */

		log.debug("title : " + title);
		log.debug("writer : " + writer);
		log.debug("price : " + price);
		log.debug("publisher : " + publisher);

		return "redirect:/param/main";
	}

	// 3.@RequestParam 여러개 파라미터얻어오기
	// - 같은 name속성값을 가진 파라미터 얻어오기( String[],List<String>
	// - 제출된 파라미터 한번에 묶어서 얻어오기 (Map<String,Object>)
	@PostMapping("test3")
	public String paramTest3(@RequestParam("color") String[] colorArr, @RequestParam("fruit") List<String> furitList,
			@RequestParam Map<String, Object> paramMap) {

		log.debug("colorArr : " + Arrays.toString(colorArr));
		log.debug("furitList : " + furitList);
		log.debug("paramMap : " + paramMap);
		// @RequestParam Map<String,Object> paramMap
		// -> 제출된 모든 파라미터가 Map에 저장된다
		// -> 같은 name 속성을 가진 파라미터는 배열이나 List 형태가 아님
		// -> 첫번째로 제출된 value값만 저장 됨
		// 즉, 첫번째로 넘어온 애만 출력

		return "redirect:/param/main";
	}

	// 4. @ModelAttribute 를 이용한 파라미터 얻어오기
	// -DTO(또는 VO)와 같이 사용하는 어노테이션

	// 전달받은 파라미터의 name속성값이
	// 함께 사용되는 DTO의 필드명과 같다면
	// 자동으로 setter를 호출해서 필드에 값을 저장
	// ** 주의 사항 **
	// -DTO에 "기본 생성자, setter" 가 필수로 존재해야함!

	// @ModelAttribute 를 이용해 값이 필드에 세팅된 객체를
	// " 커맨드 객체" 라고 부름
	// @ModelAttribute 생략 가능!!!!!!!!!!!!!!!!!!!!!!!!!!
	@PostMapping("test4")
	public String paramTest4(@ModelAttribute Meber inputMember) {

		log.debug("inputMember : " + inputMember);

		return "redirect:main";
		// 상대경로 작성법(현재 위치 중요)
		// 현재 경로의 가장 마지막 레벨의 주소값을
		// redirect: 이 주소"로 변경하기 때문
	}

}
