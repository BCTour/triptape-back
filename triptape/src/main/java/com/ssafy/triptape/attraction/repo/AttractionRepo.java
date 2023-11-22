package com.ssafy.triptape.attraction.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.tape.TapeDto;

/**
 * 관광지에 대한 mapper interface 입니다.
 */

@Mapper
public interface AttractionRepo {
	int regist(AttractionDto attraction);
	List<AttractionDto> search(SearchCondition search);
	AttractionDto info(int attractionKey);
	List<AttractionTypeDto> searchType();
	int getTotalListCount(SearchCondition search);
	
	int modify(AttractionDto attraction);
	int delete(int attractionKey);
	
	int likeAttraction(int attractionKey, String userId);
	int dislikeAttraction(int attractionKey, String userId);
	
	List<AttractionDto> userLikeAttraction(String userId);
	int userReportAttraction(String userId, int attractionKey);
	boolean isUserLikeAttraction(String userId, int attractionKey);
	boolean isUserReportAttraction(String userId, int attractionKey);
	int userDeleteReportAttraction(String userId, int attractionKey);
	
	List<AttractionDto> popularAttraction(int n);
	
	List<AttractionDto> searchReportAttraction(int reportCount);
}
