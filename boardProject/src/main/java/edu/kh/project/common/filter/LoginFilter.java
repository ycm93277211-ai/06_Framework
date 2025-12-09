package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 클래스 생성 방법]
 * 
 * 1. jakarta.servlet.filter 인터페이스 상송 받기
 * 
 * 2. doFilter() 메서드 오버라이딩
 * */

// 로그인이 되어있지 않은 경우 특정 페이지 접근 불가하도록 필터링함
public class LoginFilter implements Filter{
	
	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request,
						ServletResponse response, 
						FilterChain chain)throws IOException, ServletException {
		
		// ServletRequest : HttpServletRequest의 부모 타임
		// ServletResponse : HttpServletResponse의 부모 타임
		
		// session이 필요함 (로그인 맴버를 꺼내와야함 / session에 담겨있음)
		
		// 다운 캐스팅 하는 이유 세션(Session)을 얻어오는 기능이나 요청 URI를 얻어오는 기능을 사용하기 위해서
		// 부모타입은 저런 기능이 없기때문에 자식으로 가져와야함!!!!!!!!!
		
		
		// HTTPServletRequest 형태 (자식형태)로 다운 캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		
		//HttpServletResponse 형태 (자식형태)로 다운 캐스팅
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 현재 요청의 URI를 가져옴
		String path = req.getRequestURI(); 
		
		//  요청 URI 가  /myPage/profile 로 시작하는지 확인
		if(path.startsWith("/myPage/profile/")) {
			
			// path.startsWith : 
			// 기능: 해당 문자열(여기서는 path)이 괄호 안의 문자열(여기서는 "/myPage/profile/")로 시작하는지 검사하여, 
			// 시작하면 true를, 그렇지 않으면 false를 반환합니다.
			// 즉,"/myPage/profile/" 시작하면 필터를 통과시켜줌 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			chain.doFilter(request, response);
			// 필터를 통과한 후  return
			return;
		}
		
		// session 객체 얻어오기
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 꺼내옴
		// loginMember 있는지 null 인지 판단
		if(session.getAttribute("loginMember") == null) {
			// -> 로그인이 되어있지 않는 상태
			
			// /loginError 재요청 (resp 이용!)
			resp.sendRedirect("/loginError");
			// sendRedirect() 메서드는 클라이언트(브라우저)에게
			// 특정 URL로 다시 요청하도록 지시하는 HTTP 응답을 생성하는 역할을 합니다.
			
		}else {
			// -> 로그인이 되어있는 상태
			
			// 다음 필터로 , 또는 다음필터 없다면 DispatcherServlet으로 요청, 응답 전달
			chain.doFilter(request, response);
		}
		
		
		// FilterChain : 다음 필터 또는 DispatcherServlet과 연결된 객체
		
	}
}
