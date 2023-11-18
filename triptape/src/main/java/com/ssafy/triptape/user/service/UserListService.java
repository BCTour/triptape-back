package com.ssafy.triptape.user.service;

import java.util.List;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.tape.TapeDto;

public interface UserListService {
	List<TapeDto> userJoinTape(String userId);
	List<TapeDto> userLikeTape(String userId);
	List<AttractionDto> userLikeAttraction(String userId);
	List<RecordDto> userLikeRecord(String userId);
	int userReportAttraction(String userId, int attarctionKey);
}
