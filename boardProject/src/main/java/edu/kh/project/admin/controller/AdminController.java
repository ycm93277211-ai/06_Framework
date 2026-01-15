package edu.kh.project.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("admin")
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})
public class AdminController {
	
	private final AdminService service;
	
	@PostMapping("login")
	public Member login(@RequestBody Member inputMember, HttpSession session) {
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) return null;
		
//		model.addAttribute("loginMember", loginMember);
		// 세션에 직접 저장
	    session.setAttribute("loginMember", loginMember);
		return loginMember;
		
	}
	
	@GetMapping("logout")
	public ResponseEntity<String> logout(HttpSession session) {
		// ResponseEntity 
		// Spring에서 제공하는 Http 응답 데이터를
		// 커스터마이징 할 수 있도록 지원하는 클래스
		// -> Http 상태코드, 헤더, 응답 본문(body)을 모두 설정 가능
		try {
			session.invalidate(); // 세션 무효화 처리
			return ResponseEntity.status(HttpStatus.OK) // 200
					.body("로그아웃이 완료되었습니다");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
					.body("로그아웃 중 예외 발생 : " + e.getMessage());
		}
		
	}
	
	@PostMapping("createAdminAccount")
	public ResponseEntity<String> createAdminAccount(@RequestBody Member member){
		try {
			
			// 1. 기존에 있는 이메일인지 검사
			int checkEmail = service.checkEmail(member.getMemberEmail());
			
			// 2. 있으면 발급 안함
			if(checkEmail>0) {
				// HttpSatus.CONFLICT (409) : 요청이 서버의 현재 상태와 충돌
				// == 이미 존재하는 리소스 때문에 새로운 리소스를 만들 수 없다.
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("이미 사용중인 이메일입니다");
			}
			// 3. 없으면 새로 발급 -> 비밀번호
			String accountPw = service.createAdminAccount(member);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(accountPw);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
					.body("관리자 계정 생성 중 문제 발생(서버문의 바람)");
		
		}
	}
	
	// 관리자 계정 목록 조회
	@GetMapping("adminAccountList")
	public ResponseEntity<Object> adminAccountList(){
		try {
			
			List<Member> adminList = service.adminAccountList();
			return ResponseEntity.status(HttpStatus.OK).body(adminList);
			
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	// 최대 조회수 게시글 조회
	@GetMapping("maxReadCount")
	public ResponseEntity<Object> maxReadCount(){
		try {
			Board board =service.maxReadCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	// 최대 좋와요 게시글 조회
	@GetMapping("maxLikeCount")
	public ResponseEntity<Object> maxLikeCount(){
		try {
			Board board = service.maxLikeCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	// 최대 댓글 게시글 조회
	@GetMapping("maxCommentCount")
	public ResponseEntity<Object> maxCommentCount(){
		try {
			Board board = service.maxCommentCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	// 탈퇴 회원 리스트 조회
	@GetMapping("withdrawnMemberList")
	public ResponseEntity<Object> selectWithdrawnMemberList(){
		// 성공시 list 에러시 String 그래서 object 사용
		try {
			List<Member> withdrawnMemberList= service.selectWithdrawnMemberList();
			return ResponseEntity.status(HttpStatus.OK).body(withdrawnMemberList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴한 회원 목록 조회 중 문제 발생: " + e.getMessage());
		}
	}
	
	// 탈퇴 회원 복구
	@PutMapping("restoreMember")
	public ResponseEntity<String> restoreMember(@RequestBody Member member){
		try {
			int result  = service.restoreMember(member.getMemberNo());
			System.out.println("전달받은 객체: " + member);
		    System.out.println("전달받은 번호: " + member.getMemberNo());
		    System.out.println("DB 업데이트 결과: " + result);
			if(result > 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(member.getMemberNo()+"번 회원 복구 완료");
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("유효하지 않은 memberNo : " + member.getMemberNo());
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	@GetMapping("getBoardList")
	public ResponseEntity<Object> getBoard(){
		try {
			List<Board> getBoardList= service.getBoardList();
			return ResponseEntity.status(HttpStatus.OK).body(getBoardList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴한 회원 목록 조회 중 문제 발생: " + e.getMessage());
		}
	}
	
	@PutMapping("restoreBoard")
	public ResponseEntity<String> restoreBoard(@RequestBody Board board){
		try {
			int result  = service.restoreBoard(board.getBoardNo());
			System.out.println("전달받은 객체: " + board);
		    System.out.println("전달받은 번호: " + board.getMemberNo());
		    System.out.println("DB 업데이트 결과: " + result);
			if(result > 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(board.getBoardNo()+"번 게시글 복구 완료");
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("유효하지 않은 boardNo : " + board.getBoardNo());
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}