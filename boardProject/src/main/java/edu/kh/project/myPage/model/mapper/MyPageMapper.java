package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

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

	/** 파일 정보를 DB에 삽입 SQL
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);

	/** 프로필 이미지 변경
	 * @param member
	 * @return
	 */
	int profile(Member member);



}
