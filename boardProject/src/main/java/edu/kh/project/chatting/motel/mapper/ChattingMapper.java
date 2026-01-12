package edu.kh.project.chatting.motel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.chatting.motel.dto.ChattingRoom;
import edu.kh.project.chatting.motel.dto.Message;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface ChattingMapper {

	/** 채팅방 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅 상대 검색
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);

	/** 채팅방반호 체크
	 * @param map
	 * @return 채팅방 번호 리턴
	 */
	int checkChattingRoomNo(Map<String, Integer> map);

	/** 새로운 채팅방 생성 
	 * @param map
	 * @return 생성된 채팅방 번호
	 */
	int createChattingRoom(Map<String, Integer> map);

	/** 메세지 목록 조회
	 * @param integer
	 * @return
	 */
	List<Message> selectMessageList(Object object);

	/** 채팅 메시지 읽음 처리
	 * @param paramMap
	 * @return 
	 */
	int updateReadFlag(Map<String,Object> paramMap);

	/** 채팅 입력
	 * @param msg
	 * @return
	 */
	int insertMessage(Message msg);

}
