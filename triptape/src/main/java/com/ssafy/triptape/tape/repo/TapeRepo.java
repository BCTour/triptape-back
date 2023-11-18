package com.ssafy.triptape.tape.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.tape.TapeDto;

@Mapper
public interface TapeRepo {
	
	int registTape(TapeDto tape);
	List<TapeDto> searchTopRecent(int N);
	List<TapeDto> searchTopPopular(int N);
	List<TapeDto> searchByCondition(String keyword, String word, int start, int countPerPage);
	int getTotalListCount(String keyword, String word);
	TapeDto tapeInfo(int tapeKey);
	int deleteTape(int tapeKey);
	int updateTape(TapeDto tape);
	
	int likeTape(int tapeKey, String userId);
	int dislikeTape(int tapeKey, String userId);
	boolean isLikeTape(int tapeKey, String userId);

	int updateView(int tapeKey);
	int updateJoin(int tapeKey);
	List<TapeDto> attractionTape(int attractionKey);
	
	List<TapeDto> userJoinTape(String userId);
	List<TapeDto> userLikeTape(String userId);
	
}
