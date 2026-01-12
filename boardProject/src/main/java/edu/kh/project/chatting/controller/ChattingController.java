package edu.kh.project.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.chatting.motel.dto.ChattingRoom;
import edu.kh.project.chatting.motel.dto.Message;
import edu.kh.project.chatting.motel.service.ChattingService;
import edu.kh.project.member.model.dto.Member;

@Controller
@RequestMapping("chatting")
public class ChattingController {


	@Autowired
	private ChattingService service;

	
	// 채팅 룸
	@GetMapping("list")
	public String chatting(@SessionAttribute("loginMember") Member loginMember,Model model) {
		
		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		model.addAttribute("roomList", roomList);
		return "chatting/chatting";
	}
	
	// 채팅 상대 검색 - 비동기
	@GetMapping("selectTarget")
	@ResponseBody
	public List<Member> selectTarget(@RequestParam("query")String query,
									@SessionAttribute("loginMember") Member loginMember){
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("query", query);
		
		return service.selectTarget(map);
	}
	
	//채팅방 입장(없다면 생성) - 비동기
	@GetMapping("enter")
	@ResponseBody
	public int chattingEnter(@RequestParam("targetNo")int targetNo,
							@SessionAttribute("loginMember") Member loginMember) {
		
		Map<String , Integer> map = new HashMap<>();
		map.put("targetNo", targetNo);
		map.put("loginMemberNo", loginMember.getMemberNo());
		
		// 채팅방 번호 체크 서비스 호출 및 반환 (기존 생성된 방이 있는지)
		int chattingNo = service.checkChattingRoomNo(map);
		
		// 반환받은 채팅방의 결과값이 0(없다) 라면 생성하기
		if(chattingNo == 0) {
			chattingNo = service.createChattingRoom(map);
		}
		
		return chattingNo;
		
	}
	
	// 채팅방 목록 조회 - 비동기
	@GetMapping("roomList")
	@ResponseBody
	public List<ChattingRoom> selectRoomList(@SessionAttribute("loginMember") Member loginMember){
		return service.selectRoomList(loginMember.getMemberNo());
	}
	
	// 메세지 목록 조회 - 비동기
	@GetMapping("selectMessage")
	@ResponseBody
	public List<Message> selectMessageList(@RequestParam Map<String, Object> paramMap){
		return service.selectMessageList(paramMap);
	}
	
	@ResponseBody
	@PutMapping("updateReadFlag")
	public int updateReadFlag(@RequestBody Map<String, Object> paramMap) {
		return service.updateReadFlag(paramMap);
	}
	
	
	
}
