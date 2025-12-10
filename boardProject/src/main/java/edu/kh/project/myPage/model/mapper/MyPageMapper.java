package edu.kh.project.myPage.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/** 회원 정보 수정
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	/** 회원 암호화된 비번 조회
	 * @param memberNo
	 * @return
	 */
	String getPw(int memberNo);

	/** 비번 변경
	 * @param map
	 * @return
	 */
	int changePw(Map<String, Object> map);

	/** 회원 탈퇴
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);



}
