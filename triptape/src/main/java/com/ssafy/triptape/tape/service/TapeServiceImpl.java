package com.ssafy.triptape.tape.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.Multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.tape.TapeDto;
import com.ssafy.triptape.tape.repo.TapeRepo;

@Service
public class TapeServiceImpl implements TapeService {

	@Autowired
	private TapeRepo repo;
	
	@Autowired
	private ResourceLoader resLoader;
	
	public void fileHandling(TapeDto tape, MultipartFile file) throws IOException {
		tape.setImg(new FileInfoDto());
		
		Resource res = resLoader.getResource(tape.getImg().getSaveFolder());
		if(file != null && file.getSize() > 0) {
			
			String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			FileInfoDto fileInfo = new FileInfoDto();
			
			fileInfo.setSaveFile("http://localhost:8080/img/" + name);
			fileInfo.setOriginalFile(file.getOriginalFilename());
			
			tape.setImg(fileInfo);
			file.transferTo(new File(res.getFile().getCanonicalPath() + "/" + name));
		}
	}
	
	
	@Override
	public void registTape(TapeDto tape, MultipartFile file) throws IOException{
		fileHandling(tape, file);
		repo.registTape(tape);
	}

	@Override
	public List<TapeDto> searchTopRecent(int N) {
		return repo.searchTopRecent(N);
	}

	@Override
	public TapeDto tapeInfo(int tapeKey) {
		return repo.tapeInfo(tapeKey);
	}


	@Override
	public void updateView(int tapeKey) {
		repo.updateView(tapeKey);
	}


	@Override
	public void updateJoin(int tapeKey) {
		repo.updateJoin(tapeKey);
	}


	@Override
	public List<TapeDto> searchByCondition(String keyword, String word, int start, int countPerPage) {
		return repo.searchByCondition(keyword, word, start, countPerPage);
	}


	@Override
	public int getTotalListCount(String keyword, String word) {
		return repo.getTotalListCount(keyword, word);
	}


	@Override
	public void likeTape(int tapeKey, String userId) {
		repo.likeTape(tapeKey, userId);
	}


	@Override
	public void dislikeTape(int tapeKey, String userId) {
		repo.dislikeTape(tapeKey, userId);
	}

}
