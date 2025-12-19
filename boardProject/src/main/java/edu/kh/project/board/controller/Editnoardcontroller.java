package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")
@Slf4j
@RequiredArgsConstructor // final 붙이면 자동으로 의존성 주입
public class Editnoardcontroller {

	private final EditBoardService service;
	private final BoardService boardService;

	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		// @PathVariable : URL 경로에 포함된 변수 값을 추출
		return "board/boardWrite";
	}

	/**
	 * @param boardCode
	 * @param inputBoard  : 입력된 값(제목, 내용) 세팅되어있음 (커맨드 객체)
	 * @param loginMember : 로그인한 회원 번호를 얻어오는 용도(세션에 등록되어있음)
	 * @param images:     제출된 file 타입 input태그가 전달한 데이터를 (이미지 파일..)
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode, Board inputBoard,
			@SessionAttribute("loginMember") Member loginmember, @RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra) throws IllegalStateException, IOException {

		log.debug("images : ", images);
		// images 에 값이 없더라도
		// [MultipartFile,MultipartFile,MultipartFile,MultipartFile,MultipartFile]
		// 길이 5의 MultipartFile 가 이렇게 들어있음 MultipartFile 여기에도 값이 없음

		// - 5개 모두 업로드 O -> 0~4 인덱스에 실제 파일에 들어간 MultipartFile이 저장됨
		// - 5개 모두 업로드 X -> 0~4 인덱스에 비어있는 MultipartFile이 저장됨
		// - 2번 인덱스에만 업로드되면 MultipartFile 2번만 값 들어있음

		// - 무작정 서버에 저장 x
		// -> List 의 각 인덱스에 들어있는 MultipartFile에 실제로
		// 제출된 파일이 있는지 확인하는 로직을 구성

		// + List 요소의 index 번호 == IMG_ORDER 와 같음

		// 1. boardCode, 로그인한 회원 번호를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginmember.getMemberNo());
		// inputBoard 총 네가지 세팅됨 (boardTitle,boardContent, boardCode,memberNo

		// 2. 서비스 호출 후 결과 반환 받기
		// -> 성공 시 [상세 조회]를 요청할 수 있도록
		// 삽입된 게시글 번호를 반환받기
		int boardNo = service.boardInsert(inputBoard, images);

		// 3. 서비스 결과에 따라 message, 리다이렉트 경로 지정
		String path = null;
		String message = null;

		if (boardNo > 0) {
			path = "/board/" + boardCode + "/" + boardNo;
			message = "게시글이 작성 되었습니다.";
		} else {
			path = "insert";
			message = "게시글 작성 실패";
		}

		ra.addFlashAttribute("message", message);
		return "redirect:" + path;

	}

	/**
	 * 게시글 수정 화면 전환
	 * 
	 * @param boardCode
	 * @param boardNo
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			Model model,
			RedirectAttributes ra) {
		// 수정 화면에 출력할 기존의 제목/내용/이미지 조회
		// -> 게시글 상세 조회
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);

		// BoardService.selectOne(map) 호출 결과값 반환 받기
		Board board = boardService.selectOne(map);

		String message = null;
		String path = null;

		if (board == null) {
			message = "해당 게시글이 존재하지 않습니다";
			path = "redirect:/";

			ra.addFlashAttribute("message", message);

		} else if (board.getMemberNo() != loginMember.getMemberNo()) {

			message = "자신이 작성한 글만 수정할 수 있습니다";

			// 해당 글 상세 조회 리다이렉트(/board/1/2000)
			path = String.format("redirect;/board/%d/%d", boardCode, boardNo);

			ra.addFlashAttribute("message", message);

		} else {
			message = "해당 게시글이 존재하지 않습니다";
			path = "board/boardUpdate";
			model.addAttribute("board", board);

		}

		return path;

	}
	
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			Board inputBoard,
			@RequestParam("images")List<MultipartFile> images,
			@RequestParam(value="deleteOrderList", required = false) String deleteOrederList,
			@RequestParam(value="cp", required = false , defaultValue = "1") int cp,
			RedirectAttributes ra
			) throws Exception {
		
		// 1. 커맨드 객체에 boardCode,boardNo,memberNo 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// inputBoard -> 제목,내용,boardCode,boardNo,회원번호
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환 받기
		int result = service.boardUpdate(inputBoard,images,deleteOrederList);
		
		// 3. 서비스 결과에 따라 응답 제어
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정 되었습니다";
			path = String.format("/board/%d/%d?cp=%d",boardCode,boardNo,cp);
			// /board/1/2000/?cp = 3
		}else {
			
			message = "게시글이 수정 실패되었습니다";
			path = "update"; // GET (수정 화면 전환)
			
		}
		
		ra.addFlashAttribute("message", message);
		
				return "redirect:" + path;
		
	}
	
	@RequestMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete")
	public String boardDelete(@PathVariable("boardCode") int boardCode,
					@PathVariable("boardNo") int boardNo,
					@RequestParam(value="cp", required = false , defaultValue = "1") int cp,
					@SessionAttribute("loginMember") Member loginMember,
					RedirectAttributes ra
					) {
		
		int memberNo = loginMember.getMemberNo();
		
		int result = service.boardDelete(boardCode,boardNo,memberNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 삭제 되었습니다";
			path = String.format("/board/%d",boardCode);
			// /board/1/2000/?cp = 3
		}else {
			
			message = "게시글이 삭제 실패되었습니다";
			path = String.format("/board/%d/%d?cp=%d",boardCode,boardNo,cp);
			
		}
		
		
			ra.addFlashAttribute("message", message);
			return "redirect:" + path;
		
	}
	


}
