package edu.kh.project.admin.model.service;

import java.util.List;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;

public interface AdminService {

	Member login(Member inputMember);

	int checkEmail(String memberEmail);

	String createAdminAccount(Member member);

	List<Member> adminAccountList();

	Board maxReadCount();

	Board maxLikeCount();

	List<Member> selectWithdrawnMemberList();

	int restoreMember(int memberNo);

	Board maxCommentCount();

	List<Board> getBoardList();

	int restoreBoard(int BoardNo);

}
