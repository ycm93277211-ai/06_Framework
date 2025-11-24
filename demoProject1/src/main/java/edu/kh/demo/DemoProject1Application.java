package edu.kh.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(중요한 애들 모어둔 곳)
// Spring Boot Application에 필요한 필수 어노테이션과 설정을 모아둔 어노테이션
public class DemoProject1Application {
	
	/*
	 *  Spring Boot 프로젝트로 만든 애플리케이션의 실행을 담당하는 클래스
	 * 	Spring Application을 최소 설정으로 간단하고 빠르게 살행할 수 있게 해줌
	 * **** java 파일을 실행하듯이 Run버튼(ctrl + f11) 클릭하면 배포가 시작됨****
	 * 
	 * */
	
	

	public static void main(String[] args) {
		SpringApplication.run(DemoProject1Application.class, args);
	}

}
