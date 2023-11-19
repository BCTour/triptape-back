package com.ssafy.triptape.tape.service;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.tape.TapeDto;

public interface TapeService {
	
	int registTape(TapeDto tape, MultipartFile file) throws IOException;
	List<TapeDto> searchTopRecent(int N);
	List<TapeDto> searchTopPopular(int N);
	List<TapeDto> searchByCondition(String keyword, String word, int start, int countPerPage);
	int getTotalListCount(String keyword, String word);
	TapeDto tapeInfo(int tapeKey);
	int deleteTape(int tapeKey);
	int updateTape(TapeDto tape, MultipartFile file) throws IOException ;
	
	int updateView(int tapeKey);
	int updateJoin(int tapeKey);
	
	int likeTape(int tapeKey, String userId);
	int dislikeTape(int tapeKey, String userId);

	List<TapeDto> attractionTape(int attractionKey);
}
