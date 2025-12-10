package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		
		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공
		
		// 주소가 입력되었을 때
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
			
			
		}else { // 주소 x
			inputMember.setMemberAddress(null);
		}
		
		// inputMember :수정 닉네임 번호 주소 회원번호
		
		return mapper.updateInfo(inputMember);
	}

	// 비밀번호 변경
	@Override
	public int changePw(Map<String, Object> map) {
		
		// 번호로 갖져온 기존 비번
		String bcrcurrentPw = mapper.getPw((int)map.get("memberNo")); 
		
		// 평문
		String currentPw = (String)map.get("currentPw");
		String newPw = (String)map.get("newPw");
		// 암호화
		String bcrnewPw = bcrypt.encode(newPw);
		
		
		// 기존 비번/현재 비번 비교
		if(bcrypt.matches(currentPw, bcrcurrentPw)) { //암호화된 일치하면
			// 새로운 비번 대입
			map.put("newPw", bcrnewPw);
			
		}else {
			return 0;
		}
			
		
		return mapper.changePw(map);
	}

	// 회원 탈퇴 서비스
	@Override
	public int secession(String memberPw, Member loginMember) {

		
		// 1. 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String encPw =mapper.getPw( loginMember.getMemberNo());
		
		// 2. 입력받은 비번 & 암호화된 DB 비번 같은지 비교
		
		// 다를 경우
		if(!bcrypt.matches(memberPw, encPw)) {
			return 0;
		}else { // 같을 경우
			return mapper.secession(loginMember.getMemberNo());
		}
				
		
		
	}

	// 파일 업로드 테스트 1
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception {
		
		// 업로드한 파일이 없을 경우
		if(uploadFile.isEmpty()) { 
			return null;
		}
		
		// 업로드한 파일이 있을 경우
		// C:/uploadFile/test/파일명으로 서버에 저장
		uploadFile.transferTo(new File("C:/uploadFiles/test/"
		+ uploadFile.getOriginalFilename()));
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 만들어 반환
		// 이미지가 최종 저장된 서버 컴퓨터상의 경로
		// C:/uploadFile/test/파일명.jpg
		
		// 클라이언트가 브라우저에 해당 이미지를 보기위해 요청하는 경로
		// <img src="경로">
		// /myPage/file/파일명.jpg -> <img src="/myPage/file/파일명.jpg">
		
		return "/myPage/file/"+uploadFile.getOriginalFilename();
	}


	
	
	
	
	
	
	
	
	
}
