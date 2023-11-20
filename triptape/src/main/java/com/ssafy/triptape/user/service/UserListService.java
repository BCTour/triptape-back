package com.ssafy.triptape.user.service;

import java.util.List;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.tape.TapeDto;

public interface UserListService {
	List<TapeDto> userJoinTape(String userId);
	List<TapeDto> userLikeTape(String userId);
	List<RecordDto> userLikeRecord(String userId);
	List<AttractionDto> userLikeAttraction(String userId);
	int userReportAttraction(String userId, int attarctionKey);
	List<AttractionDto> searchReportAttraction(int reportCount);
	
	boolean isUserLikeAttraction(String userId, int attractionKey);
	boolean isUserLikeRecord(String userId, int recordKey, int tapeKey);
	boolean isUserLikeTape(String userId, int tapeKey);
}
