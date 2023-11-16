package com.ssafy.triptape.attraction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.triptape.attraction.AttractionComment;
import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.repo.AttractionCommentRepo;

@Service
public class AttractionCommentServiceImpl implements AttractionCommentService {

	@Autowired
	AttractionCommentRepo repo;

	/**
	 * 관광지 댓글 검색
	 * 
	 * @Param attarctionKey(관광지 코드), start(시작페이지), countPerPage(페이지별 댓글 수)
	 * @return List<AttractionComment> - 관광지댓글 리스트
	 * */
	@Override
	public List<AttractionComment> search(int attractionKey, int start, int countPerPage) {
		return repo.search(attractionKey, start, countPerPage);
	}

	@Override
	@Transactional
	public int regist(AttractionComment comment) {
		return repo.regist(comment);
	}

	@Override
	public int getTotalListCount(int attractionKey) {
		return repo.getTotalListCount(attractionKey);
	}

	@Override
	@Transactional
	public int modify(AttractionComment comment) {
		return repo.modify(comment);
	}

	@Override
	@Transactional
	public int delete(int commmentKey) {
		// TODO Auto-generated method stub
		return repo.delete(commmentKey);
	}
	


}
