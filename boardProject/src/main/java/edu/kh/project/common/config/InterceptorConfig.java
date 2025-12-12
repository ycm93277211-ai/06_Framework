package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // 필수 import

import edu.kh.project.common.interceptor.BoardTypeInterceptor;

// 인터셉터가 어떤 요청을 가로챌지 설정하는 클래스

@Configuration // 서버가 켜지면 내부 @Bean 메서드를 모두 수행하고, 설정을 등록함
// WebMvcConfigurer를 구현하여 스프링 MVC 설정을 정의합니다.
public class InterceptorConfig implements WebMvcConfigurer { // <-- WebMvcConfigurer 구현

	@Bean // BoardTypeInterceptor 객체를 생성하여 스프링 컨테이너에 Bean으로 등록
	// 개발자가 수동으로 만든 객체 -> 관리는 Spring Container가 수행
	public BoardTypeInterceptor boardTypeInterceptor() {
		return new BoardTypeInterceptor();
	}

	// WebMvcConfigurer 인터페이스의 메서드를 오버라이드하여 인터셉터 등록
	// 동작할 인터셉터 객체를 추가하는 메서드
	@Override  
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(boardTypeInterceptor()) // 등록할 인터셉터 객체
		.addPathPatterns("/**") // 가로챌 요청 주소를 지정 (/** : / 이하 모든 요청)
		// 정적 리소스 요청은 가로채지 않도록 예외 처리
		.excludePathPatterns("/css/**","/js/**","/images/**","/favicon.ico");
		// 가로채지 않을 요청 주소를 지정(정적리소스 요청은 가로채지 않음)
	}
}