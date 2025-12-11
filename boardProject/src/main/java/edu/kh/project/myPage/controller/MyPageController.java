package edu.kh.project.myPage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import edu.kh.project.common.config.SecurityConfig;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

//SessionAttributes의 역할
// -Model 추가된 속성 중 key 값이 일치하는 속성을 session socpe로 변경하는 어노테이션

//SessionAttribute 의 역할
// - @SessionAttributes를 통해 session에 등록된속성을 꺼내올 때 사용하는 어노테이션
// - 메서드의 매개변수에 @SessionAttribute("lodinMember) Member loginMember 작성

@SessionAttributes({ "loginMember" })
@Slf4j
@Controller
@RequestMapping("myPage")
public class MyPageController {

	private final SecurityConfig securityConfig;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MyPageService service;

	MyPageController(BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConfig securityConfig) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.securityConfig = securityConfig;
	}

	// 내 정보 조회
	/**
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 Member 타입 매개변수 대입
	 * @return
	 */
	@GetMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {

		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보-> session scope에 등록된 상태 (loginMember)
		// loginMember(memberAddress도 포함)
		// -> 만약 회원가입 당시 주소를 입력했다면 주소값 문자열(^^^ 구분자로 만들어진 문자열)
		// -> 회원가입 당시 주소를 입력하지 않았다면 null

		String memberAddress = loginMember.getMemberAddress();
		// 52006^^^경남 함안군 칠북면 경남대로 2368-1^^^12층 1204

		if (memberAddress != null) {// 주소가 있을 경우에만 동작
			// 구분자 ^^^ 를 기준으로
			// memberAddress 값을 쪼개어 String[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// 52006,경남 함안군 칠북면 경남대로 2368-1,12층 1204

			model.addAttribute("postcode", arr[0]); // 우편 주소
			model.addAttribute("address", arr[1]); // 도로명/지번주소
			model.addAttribute("detailAddress", arr[2]); // 상세 주소

		}

		return "myPage/myPage-info";
	}

	// 프로필 이미지 변경 화면으로 이동
	@GetMapping("profile")
	public String profile() {
		return "myPage/myPage-profile";
	}

	// 비밀번호 변경
	@GetMapping("changePw")
	public String changePw() {

		return "myPage/myPage-changePw";
	}

	// 비밀번호 변경 !!!
	@PostMapping("changePw")
	public String changePw(@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("currentPw") String currentPw, @RequestParam("newPw") String newPw,
			@RequestParam("newPwConfirm") String newPwConfirm, RedirectAttributes ra) {

		Map<String, Object> map = new HashMap<>();

		map.put("currentPw", currentPw);
		map.put("newPw", newPw);
		map.put("memberNo", loginMember.getMemberNo());

		String message = null;
		String path = "myPage/myPage-changePw";

		if (!newPw.equals(newPwConfirm)) {
			message = "새로운 비밀번호가 같지 않음";
			ra.addAttribute("message", message);
			return path;
		}

		int result = service.changePw(map);

		if (result > 0) { // 수정 성공
			message = "변경 성공";
		} else {
			message = "변경 실패";
			return path;
		}

		return "redirect:/";
	}

	// 회원 탈퇴 화면 이동
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}

	/**
	 * 회원 탈퇴
	 * 
	 * @param memberPw    : 제출받은(입력한 ) 비밀번호
	 * @param loginMember : 로그인 회원 정보 저장 객체 (세션에서 꺼내옴) -> 회원번호 필요!(SQL에서 조건으로 사용)
	 * @return
	 */
	@PostMapping("secession")
	public String secession(@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember, SessionStatus status, RedirectAttributes ra) {

		// 로그인한 회원의 회원번호 꺼내오기
		int memberNo = loginMember.getMemberNo();

		// 서비스 호출(입력받은 비밀번호, 로그인한 회원번호)
		int result = service.secession(memberPw, loginMember);

		String message = null;
		String path = null;

		if (result > 0) {
			message = "탈퇴 되었습니다.";
			path = "/";

			status.setComplete(); // 세션 비우기 (로그아웃 상태 변경)
		} else {
			message = "탈퇴 안 되었습니다.";
			path = "secession";
		}

		// 탈퇴 성공 - 메인페이지 재요청
		// 탈퇴 실패 - 탈퇴 페이지로 재요청
		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}

	// 파일 테스트 화면으로 이동
	@GetMapping("fileTest")
	public String fileTest() {
		return "myPage/myPage-fileTest";
	}

	// 파일 업로드 테스트 1
	@PostMapping("file/test1")
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile, RedirectAttributes ra) {
		/*
		 * String에서 파일을 처리하는 방법
		 * 
		 * - enctype = "multipart/form-data" 로 클라이언트의 요청을 받으면 (문자,숫자,파일 등이 섞여있는 요청)
		 * 
		 * 이를 MultipartResolver(FileConfig에 정의)를 이용해서 섞여있는 파라미터를 분리 작업을 함
		 * 
		 * 문자열, 숫자 -> String 파일 -> MultipartFile
		 * 
		 */
		String path;
		try {
			path = service.fileUpload1(uploadFile);

			if (path != null) {
				ra.addFlashAttribute("path", path);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// /myPage/file/파일명.jpg

		// 파일이 실제로 서버 컴퓨터에 저장이 되어
		// 웹에서 접근할 수 있는 경로가 반환되었을 때

		return "redirect:/myPage/fileTest";
	}

	// 파일 업로드 테스트 2
	@PostMapping("file/test2")
	public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile,
			@SessionAttribute("loginMember") Member loMember, RedirectAttributes ra) {

		try {

			// 로그인한 회원의 번호 얻어오기(누가 업로드 했는가)
			int memberNo = loMember.getMemberNo();

			// 업로드된 파일 정보를 DB에 INSERT 후 결과 행의 갯수 반환 받아옴
			int result = service.fileUpload2(uploadFile, memberNo);

			String message = null;

			if (result > 0) {
				message = "파일 업로드 성공";
			} else {
				message = "파일 업로드 실패";
			}

			ra.addFlashAttribute("message", message);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 업로드 테스트2 중 예외발생");
		}
		return "redirect:/myPage/fileTest";
	}
	
	// 파일 업로드 테스트 2
	@PostMapping("file/test3")
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList,
							@RequestParam("bbb")List<MultipartFile> bbbList,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra)throws Exception {
		
		// aaa 파일 미체출 시
		// 0번 1번, 인덱스로 구성 - 파일은 모두 비어있음
//		log.debug("aaaList"+aaaList); // [요소,요소]
		
		// bbb(multiple) 파일 미제출 시
		// 0번 인덱스로 구성 - 파일이 비어있음
//		log.debug("bbbList"+bbbList);// [요소]
		
		// 여러 파일 업로드 서비스 호출
		int result = service.fileUpload3(aaaList,bbbList,loginMember.getMemberNo());
		
		String message = null;
		
		if(result ==0 ) {
			message ="업로드된 파일이 없습니다.";
		}else {
			message ="업로드된 파일이 있습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";
	}

	// 파일 목록 조회 화면 이동
	@GetMapping("fileList")
	public String fileList(@SessionAttribute ("loginMember") Member loginMember,Model model) throws Exception{

		// 파일 목록 조회 서비스 호출 (현재 로그인한 회원이 올린 이미지만)
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		// model에 list 담아서 forward
		model.addAttribute("list", list);
		
		return "myPage/myPage-fileList";
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @return
	 */
	@PostMapping("info")
	public String infot(Member inputMember, @RequestParam("memberAddress") String[] memberAddress,
			@SessionAttribute("loginMember") Member logiMember, RedirectAttributes ra) {

		// inputMember에 현재 로그인 회원 번호 추가
		inputMember.setMemberNo(logiMember.getMemberNo());
		// inputMember : 수정된 회원의 닉네임 , 수정된 회원의 전화번호, [주소],회원번호

		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);

		String message = null;

		if (result > 0) {
			message = "회원 정보 수정 성공!!";

			// loginMember에 DB상 업데이트된 내용으로 세팅
			// -> loginMember는 세션에 저장된 로그인한 회원 정보가
			// 저장되어있다( 로그인 할 당시의 기존 데이터)
			// -> loginMember를 수정하면 세션에 저장된 로그인한 회원의 정보가 업데이트 된다
			// == Session에 있는 회원 정보와 DB 데이터를 동기화

			// 즉,기존 loginMember를 DB상의 데이터로 갱신 시키기
			logiMember.setMemberNickname(inputMember.getMemberNickname());
			logiMember.setMemberTel(inputMember.getMemberTel());
			logiMember.setMemberAddress(inputMember.getMemberAddress());

		} else {
			message = "회원 정보 수정 실패..";
		}

		ra.addAttribute("message", message);

		return "redirect:/";
	}
	
	// 프로필 이미지
	@PostMapping("profile")
	public String profile(@RequestParam("profileImg") MultipartFile profileImg,
							@SessionAttribute("loginMember")Member loginMember,
							RedirectAttributes ra) throws Exception {
		
		// 서비스 호출
		int result = service.profile(profileImg,loginMember);
		
		String message = null;
		
		if(result > 0) {
			message = "변경 성공";
		}else {
			message = "변경 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";
	}

}
