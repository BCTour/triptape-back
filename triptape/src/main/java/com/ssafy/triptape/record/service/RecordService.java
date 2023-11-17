package com.ssafy.triptape.record.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.record.RecordDto;


public interface RecordService {
	int registRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException ;
}
