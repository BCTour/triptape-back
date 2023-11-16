package com.ssafy.triptape.tape.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.tape.TapeDto;

@Mapper
public interface TapeRepo {
	
	void registTape(TapeDto tape);
	List<TapeDto> searchTopRecent(int N);
	List<TapeDto> searchTopPopular(int N);
	List<TapeDto> searchByCondition(TapeDto tape);
	TapeDto tapeInfo(int tapeKey);
}
