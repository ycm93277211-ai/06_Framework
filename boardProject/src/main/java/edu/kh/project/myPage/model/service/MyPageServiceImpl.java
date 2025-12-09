package edu.kh.project.myPage.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


	
	
	
	
	
	
	
	
	
}
