package com.ssafy.triptape.attraction.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;

public interface AttractionService {
	int regist(AttractionDto attraction, MultipartFile file)throws IOException;
	List<AttractionDto> search(SearchCondition search);
	AttractionDto info(int attractionKey);
	List<AttractionTypeDto> searchType();
	int getTotalListCount(SearchCondition search);
	
	int modify(AttractionDto attraction, MultipartFile file) throws IOException;
	int delete(int attractionKey);
	
	int likeAttraction(int attractionKey, String userId);
	int dislikeAttraction(int attractionKey, String userId);
	
	List<AttractionDto> popularAttraction(int n);
}
