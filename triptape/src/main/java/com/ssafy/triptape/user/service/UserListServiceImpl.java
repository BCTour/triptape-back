package com.ssafy.triptape.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.repo.AttractionRepo;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.record.repo.RecordRepo;
import com.ssafy.triptape.tape.TapeDto;
import com.ssafy.triptape.tape.repo.TapeRepo;

@Service
public class UserListServiceImpl implements UserListService {

	@Autowired
	private AttractionRepo attractionRepo;
	
	@Autowired
	private TapeRepo tapeRepo;
	
	@Autowired
	private RecordRepo recordRepo;
	
	
	@Override
	public List<TapeDto> userJoinTape(String userId) {
		return tapeRepo.userJoinTape(userId);
	}

	@Override
	public List<TapeDto> userLikeTape(String userId) {
		return tapeRepo.userLikeTape(userId);
	}

	@Override
	public List<AttractionDto> userLikeAttraction(String userId) {
		return attractionRepo.userLikeAttraction(userId);
	}

	@Override
	public List<RecordDto> userLikeRecord(String userId) {
		return recordRepo.userLikeRecord(userId);
	}

	@Override
	public int userReportAttraction(String userId, int attarctionKey) {
		return attractionRepo.userReportAttraction(userId, attarctionKey);
	}

	@Override
	public List<AttractionDto> searchReportAttraction(int reportCount) {
		return attractionRepo.searchReportAttraction(reportCount);
	}

	@Override
	public boolean isUserLikeAttraction(String userId, int attractionKey) {
		return attractionRepo.isUserLikeAttraction(userId, attractionKey);
	}

	@Override
	public boolean isUserLikeRecord(String userId, int recordKey, int tapeKey) {
		return recordRepo.isUserLikeRecord(userId, recordKey, tapeKey);
	}

	@Override
	public boolean isUserLikeTape(String userId, int tapeKey) {
		return tapeRepo.isUserLikeTape(userId, tapeKey);
	}
}
