package edu.kh.project.myPage.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;

public interface MyPageService {

	/** 회원 정보 수정 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member inputMember, String[] memberAddress);



	/** 비밀번호 변경
	 * @param map
	 * @return
	 */
	int changePw(Map<String, Object> map);



	/** 회원 탈퇴
	 * @param memberPw
	 * @param loginMember
	 * @return
	 */
	int secession(String memberPw, Member loginMember);



	/** 파일 업로드 테스트 1
	 * @param uploadFile
	 * @return
	 */
	String fileUpload1(MultipartFile uploadFile)throws Exception ;


}
