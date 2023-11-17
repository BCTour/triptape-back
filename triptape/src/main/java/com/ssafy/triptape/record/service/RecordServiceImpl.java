package com.ssafy.triptape.record.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	public int registRecord(RecordDto record, MultipartFile file) throws IllegalStateException, IOException {
		fileHandling(record,file);
		return repo.registRecord(record);
	}

}
