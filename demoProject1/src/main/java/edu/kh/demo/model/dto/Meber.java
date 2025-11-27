package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // get,set,toString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 매개변수 생성자
public class Meber {
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;

}
