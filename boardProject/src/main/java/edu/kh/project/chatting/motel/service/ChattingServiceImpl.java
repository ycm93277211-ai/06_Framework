package edu.kh.project.chatting.motel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.chatting.motel.dto.ChattingRoom;
import edu.kh.project.chatting.motel.dto.Message;
import edu.kh.project.chatting.motel.mapper.ChattingMapper;
import edu.kh.project.member.model.dto.Member;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChattingServiceImpl implements ChattingService {

	@Autowired
	private ChattingMapper mapper;
	
	@Override
	public List<ChattingRoom> selectRoomList(int memberNo) {
		return mapper.selectRoomList(memberNo);
	}

	// 채팅 상대 검색 서비스
	@Override
	public List<Member> selectTarget(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.selectTarget(map);
	}

	// 체팅방번호 체크
	@Override
	public int checkChattingRoomNo(Map<String, Integer> map) {
		return mapper.checkChattingRoomNo(map);
	}
	// 새로운 채팅방 생성 서비스
	@Override
	public int createChattingRoom(Map<String, Integer> map) {
		
		int result = mapper.createChattingRoom(map);
		
		if(result > 0) {
			return map.get("chattingRoomNo");
		}
		return 0;
	}

	// 채팅 메시지 조회 서비시
	@Override
	public List<Message> selectMessageList(Map<String, Object> paramMap) {

		List<Message> messageList = mapper.selectMessageList(paramMap.get("chattingRoomNo"));
		
		if(!messageList.isEmpty()) {
			// 해당 채팅방에 나눈 메시지가 있다면 
			mapper.updateReadFlag(paramMap);
			
		}
		
		return messageList;
	}

	// 읽음 표시
	@Override
	public int updateReadFlag(Map<String, Object> paramMap) {
		return mapper.updateReadFlag(paramMap);
	}

	// 채팅입력
	@Override
	public int insertMessage(Message msg) {
		return mapper.insertMessage(msg);
	}



	
}
