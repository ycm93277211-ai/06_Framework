package edu.kh.project.admin.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface AdminMapper {

	Member login(String memberEmail);

	int checkEmail(String memberEmail);

	int createAdminAccount(Member member);

	List<Member> adminAccountList();

	Board maxReadCount();

	Board maxLikeCount();

	List<Member> selectWithdrawnMemberList();

	int restoreMember(int memberNomemberNo);

	Board maxCommentCount();

	List<Board> getBoardList();

	int getrestorBoard(int boardNo);

	int restoreBoard(int boardNo);

}
