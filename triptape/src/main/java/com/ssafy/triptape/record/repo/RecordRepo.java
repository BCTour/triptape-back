package com.ssafy.triptape.record.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.record.RecordDto;

@Mapper
public interface RecordRepo {
	
	int registRecord(RecordDto record);
	List<AttractionDto> recordAttraction(int tapeKey, String keyword, String word, int start, int countPerPage);
	List<RecordDto> searchRecord(int tapeKey,String keyword, String word, int start, int countPerPage);
	RecordDto recordInfo(int recordKey);
	int deleteRecord(int recordKey);
	int modifyRecord(RecordDto record);
	void likeRecord(int recordKey, String userId);
	void dislikeRecord(int recordKey, String userId);

}
