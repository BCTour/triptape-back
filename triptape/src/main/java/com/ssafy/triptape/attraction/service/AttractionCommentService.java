package com.ssafy.triptape.attraction.service;


import java.util.List;

import com.ssafy.triptape.attraction.AttractionComment;
import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;

public interface AttractionCommentService {
	List<AttractionComment> search(int attractionKey,int start, int countPerPage);
	int regist(AttractionComment comment);
	int getTotalListCount(int attractionKey);
	
	int modify(AttractionComment comment);
	int delete(int commmentKey);
}
