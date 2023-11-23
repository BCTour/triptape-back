package com.ssafy.triptape.record.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.record.repo.RecordRepo;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	ResourceLoader resLoader;
	
	@Autowired
	private RecordRepo repo;
	
	private void fileHandling(RecordDto record, MultipartFile file) throws IllegalStateException, IOException {
		record.setImg(new FileInfoDto());
		
		Resource res = resLoader.getResource(record.getImg().getSaveFolder());
		if(file != null && file.getSize() > 0) {
			String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			
			record.getImg().setSaveFile("http://localhost:8080/img/" + name);
			record.getImg().setOriginalFile(file.getOriginalFilename());
			
			file.transferTo(new File(res.getFile().getCanonicalPath() + "/" + name));
		}
	}
	
	
	@Override
	@Transactional
	public int registRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException {
		fileHandling(record,file);
		int recordKey = repo.getRecordKey((record.getTapeKey()));
		record.setRecordKey(recordKey + 1);
		return repo.registRecord(record);
	}

	@Override
	public List<AttractionDto> recordAttraction(int tapeKey, String keyword, String word, int start, int countPerPage) {
		return repo.recordAttraction(tapeKey, keyword, word, start, countPerPage);
	}

	@Override
	public List<RecordDto> searchRecord(int tapeKey,String keyword, String word, int start, int countPerPage) {
		return repo.searchRecord(tapeKey, keyword, word, start, countPerPage);
	}

	@Override
	public RecordDto recordInfo(int tapeKey, int recordKey) {
		return repo.recordInfo(tapeKey, recordKey);
	}
	@Override
	@Transactional
	public int deleteRecord(int tapeKey, int recordKey) {
		return repo.deleteRecord(tapeKey, recordKey);
	}

	@Override
	@Transactional
	public int modifyRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException {
		fileHandling(record, file);
		return repo.modifyRecord(record);
	}

	@Override
	@Transactional
	public int likeRecord(int tapeKey, int recordKey, String userId) {
		return repo.likeRecord(tapeKey, recordKey, userId);
	}

	@Override
	@Transactional
	public int dislikeRecord(int tapeKey, int recordKey, String userId) {
		return repo.dislikeRecord(tapeKey, recordKey, userId);
	}
	

}
