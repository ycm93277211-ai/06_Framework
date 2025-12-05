package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그 찍는놈
@Service
@Transactional(rollbackFor = Exception.class) // 수행되면 커밋, 안되면 롤백
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper mapper;
	
	//  Bcrypt 암호화 객체 의존성 주입(SecurityConfig 클래스 참고)
	// 알아서 암호화하고 비교해줌
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember){
		
		// 암호화 진행(TEST)	
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환
		//String bcryptPasswore = bcrypt.encode(inputMember.getMemberPw());
		//log.debug("bcryptPasswore : "+bcryptPasswore);
		
		
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원의 (비밀번호) 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null
		if(loginMember == null) return null;
		
		// 3. 입력 받은 비밀번호 (평문  : inputMember.getMemberPw()) 와
		//    암호화된 비밀번호(loginMember.getMemberPw())
		// 	  두 비밀번호가 일치하는지 확인
		
		// bcrypt.matches(평문,암호화) : 평문과 암호화가 내부적으로 일치한다고 
		// 판단이 되면 true, 아니면 false
		
		// 일치하지 않으면
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
		
		// 로그인한 회원 정보에서 비밀번호 제거
		loginMember.setMemberPw(null);
		
		
		return loginMember;
	}

}
