package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO (Data Transfer Object
// 데이터 전달용 객체
// DB에 조합된 결과 또는 SQL 구문에 사용할 값을 전달하는 용도

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	// DB 컬럼 이름이랑 똑같이 만들기(카멜케이스으로 적기  왜 적냐 - > @Model 사용할려고 )
	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String enrollDate;
	private String memberDelfl;
	private int authority;
	
}
