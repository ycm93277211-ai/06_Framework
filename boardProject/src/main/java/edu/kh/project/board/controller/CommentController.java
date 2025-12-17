package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;

//@Controller + @ResponseBody = @RestController(모든 요청에 대한 응답을 본문으로 반환하는 컨트롤러)
// @RestController 즉, 모든 요청이 비동기 일 때 사용

@RestController // REST API 구축을 위해서 사용하는 컨트롤러
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentService service;
	
	
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("")
	public List<Comment> select(@RequestParam("boardNo") int boardNo){
		
		
		return service.select(boardNo);
		
	}
	
	/** 댓글 또는 답글
	 * + 답글일 때는 parentCommentNo 포함
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		
		return service.insert(comment);
	}
	
	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */
	@DeleteMapping("")
	public int deiete(@RequestBody int commentNo) {
		
		return service.delete(commentNo);
	}
	
	
	/** 댓글 수정
	 * @param comment
	 * @return
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		
		return service.update(comment);
	}
}
