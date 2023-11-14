package com.ssafy.triptape.attraction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.triptape.attraction.AttractionComment;
import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.repo.AttractionCommentRepo;

@Service
public class AttractionCommentServiceImpl implements AttractionCommentService {

	@Autowired
	AttractionCommentRepo repo;

	@Override
	public List<AttractionComment> search(int attractionKey, int start, int countPerPage) {
		return repo.search(attractionKey, start, countPerPage);
	}

	@Override
	public int regist(AttractionComment comment) {
		return repo.regist(comment);
	}

	@Override
	public int getTotalListCount(int attractionKey) {
		return repo.getTotalListCount(attractionKey);
	}

	@Override
	public int modify(AttractionComment comment) {
		return repo.modify(comment);
	}

	@Override
	public int delete(int commmentKey) {
		// TODO Auto-generated method stub
		return repo.delete(commmentKey);
	}
	


}
