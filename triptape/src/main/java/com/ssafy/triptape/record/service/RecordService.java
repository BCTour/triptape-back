package com.ssafy.triptape.record.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.record.RecordDto;


public interface RecordService {
	int registRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException;
	List<AttractionDto> recordAttraction(int tapeKey, String keyword, String word,int start, int countPerPage);
	List<RecordDto> searchRecord(int tapeKey,String keyword, String word, int start, int countPerPage);
	RecordDto recordInfo(int recordKey);
	int deleteRecord(int recordKey);
	int modifyRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException;
	void likeRecord(int recordKey, String userId);
	void dislikeRecord(int recordKey, String userId);
}
