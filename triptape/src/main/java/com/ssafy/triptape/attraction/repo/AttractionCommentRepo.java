package com.ssafy.triptape.attraction.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.attraction.AttractionComment;

/**
 * 관광지 댓글에 대한 mapper interface 입니다.
 */
@Mapper
public interface AttractionCommentRepo {
	List<AttractionComment> search(int attractionKey,int start, int countPerPage);
	int regist(AttractionComment comment);
	int getTotalListCount(int attractionKey);
	
	int modify(AttractionComment comment);
	int delete(int commmentKey);
	
	AttractionComment commentInfo(int commentKey);
}
