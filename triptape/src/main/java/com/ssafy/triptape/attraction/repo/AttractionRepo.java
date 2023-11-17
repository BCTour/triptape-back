package com.ssafy.triptape.attraction.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;

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
	
	void likeAttraction(int attractionKey, String userId);
	void dislikeAttraction(int attractionKey, String userId);
}
