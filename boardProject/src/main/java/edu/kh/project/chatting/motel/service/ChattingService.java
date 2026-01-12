package edu.kh.project.chatting.motel.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.chatting.motel.dto.ChattingRoom;
import edu.kh.project.chatting.motel.dto.Message;
import edu.kh.project.member.model.dto.Member;

public interface ChattingService {

	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅상태 검색
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);

	/** 기존의 채팅방번호 체크 서비스
	 * @param map
	 * @return
	 */
	int checkChattingRoomNo(Map<String, Integer> map);

	/** 없다면 채팅방 만들기
	 * @param map
	 * @return
	 */
	int createChattingRoom(Map<String, Integer> map);

	/** 채팅 메세지 조회 서비스
	 * @param paramMap
	 * @return
	 */
	List<Message> selectMessageList(Map<String, Object> paramMap);

	/** 채팅 읽음 표시
	 * @param paramMap
	 * @return
	 */
	int updateReadFlag(Map<String, Object> paramMap);

	/** 채팅 입력 서비스
	 * @param msg
	 * @return
	 */
	int insertMessage(Message msg);



}
