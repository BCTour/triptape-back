package com.ssafy.triptape.tape.service;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.tape.TapeDto;

public interface TapeService {
	
	void registTape(TapeDto tape, MultipartFile file) throws IOException;
	List<TapeDto> searchTopRecent(int n);
	List<TapeDto> searchByCondition(String keyword, String word, int start, int countPerPage);
	int getTotalListCount(String keyword, String word);
	TapeDto tapeInfo(int tapeKey);

	void updateView(int tapeKey);
	void updateJoin(int tapeKey);
	
	void likeTape(int tapeKey, String userId);
	void dislikeTape(int tapeKey, String userId);
}
