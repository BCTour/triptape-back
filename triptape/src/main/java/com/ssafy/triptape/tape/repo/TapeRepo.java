package com.ssafy.triptape.tape.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.tape.TapeDto;

@Mapper
public interface TapeRepo {
	
	void registTape(TapeDto tape);
	List<TapeDto> searchTopRecent(int N);
	List<TapeDto> searchTopPopular(int N);
	List<TapeDto> searchByCondition(String keyword, String word, int start, int countPerPage);
	int getTotalListCount(String keyword, String word);
	TapeDto tapeInfo(int tapeKey);
	int deleteTape(int tapeKey);
	int updateTape(TapeDto tape);
	
	void likeTape(int tapeKey, String userId);
	void dislikeTape(int tapeKey, String userId);
	
	void updateView(int tapeKey);
	void updateJoin(int tapeKey);
	
}
