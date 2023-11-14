package com.ssafy.triptape.attraction.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.attraction.AttractionComment;

@Mapper
public interface AttractionCommentRepo {
	List<AttractionComment> search(int attractionKey,int start, int countPerPage);
	int regist(AttractionComment comment);
	int getTotalListCount(int attractionKey);
	
	int modify(AttractionComment comment);
	int delete(int commmentKey);
}
