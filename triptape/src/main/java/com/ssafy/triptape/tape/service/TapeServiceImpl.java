package com.ssafy.triptape.tape.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.Multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
			
			tape.getImg().setSaveFile("http://localhost:8080/img/" + name);
			tape.getImg().setOriginalFile(file.getOriginalFilename());
			
			file.transferTo(new File(res.getFile().getCanonicalPath() + "/" + name));
		}
	}
	
	
	@Override
	@Transactional
	public int registTape(TapeDto tape, MultipartFile file) throws IOException{
		fileHandling(tape, file);
		return repo.registTape(tape);
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
	@Transactional
	public int updateView(int tapeKey) {
		return repo.updateView(tapeKey);
	}


	@Override
	@Transactional
	public int updateJoin(int tapeKey) {
		return repo.updateJoin(tapeKey);
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
	@Transactional
	public int likeTape(int tapeKey, String userId) {
		return repo.likeTape(tapeKey, userId);
	}


	@Override
	@Transactional
	public int dislikeTape(int tapeKey, String userId) {
		return repo.dislikeTape(tapeKey, userId);
	}


	@Override
	public List<TapeDto> searchTopPopular(int N) {
		return repo.searchTopPopular(N);
	}


	@Override
	@Transactional
	public int deleteTape(int tapeKey) {
		return repo.deleteTape(tapeKey);
	}


	@Override
	@Transactional
	public int updateTape(TapeDto tape, MultipartFile file) throws IOException {
		fileHandling(tape, file);
		return repo.updateTape(tape);
	}


	@Override
	public List<TapeDto> attractionTape(int attractionKey) {
		return repo.attractionTape(attractionKey);
	}

}
