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


	// 이메일 중복 검사 서비스
	@Override
	public int checkEmail(String memberEmail) {
		// TODO Auto-generated method stub
		return mapper.checkEmail(memberEmail);
	}

	// 닉네임 중복 검사
	@Override
	public int checkNickname(String memberNickname) {
		// TODO Auto-generated method stub
		return  mapper.checkNickname( memberNickname);
	}

	// 회원가입 서비스
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 1. 주소 배열 -> 하나의 문자열로 가공
		// 주소가 입력되지 않았다면 
		// inputMemeber.getMemberAddress() -> ",,"
		// memberAddress ->[,,]
		
		// 주소가 입력된 경우
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			// String.join("구분자", 배열)
			// -> 배열의 모든 요소 사이에 "구분자"를 추가하여
			//		하나의 문자열로 만들어 반환하는 메서드
			
			// 구분자는 남들이 쓰지 않는걸로 해야함!
			String address = String.join("^^^", memberAddress);
			
			// inputMember의 주소값을 위에서 만든 주소로 세팅
			inputMember.setMemberAddress(address);
			
		}
		// 주소가 입력되지 않은 경우
		else{
			inputMember.setMemberAddress(null);// null 저장(DB에 null 저장)
		}
		
		
		
		// 2. 비밀번호 암호화
		// intputMember 안의 memberPw -> 평문
		// 비밀번호를 암호화하여 inputMember에 세팅
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// 회원가입 매퍼 메서드 호출
		return mapper.signup(inputMember);
	}

}
