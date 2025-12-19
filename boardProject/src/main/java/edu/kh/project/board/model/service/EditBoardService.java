package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 작성 서비스
	 * @param inputBoard
	 * @param images
	 * @return
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images)throws IllegalStateException, IOException;

	/** 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrederList
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrederList) throws IllegalStateException, IOException;

	/** 게시글 삭제
	 * @param boardCode
	 * @param boardNo
	 * @param cp
	 * @return
	 */
	int boardDelete(int boardCode, int boardNo, int cp);

}
