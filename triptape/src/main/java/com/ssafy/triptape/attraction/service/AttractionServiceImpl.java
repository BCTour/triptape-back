package com.ssafy.triptape.attraction.service;

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
import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.repo.AttractionRepo;
import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.user.UserDto;

@Service
public class AttractionServiceImpl implements AttractionService {
	@Autowired
	ResourceLoader resLoader;
	@Autowired
	private AttractionRepo repo;
	
	private void fileHandling(AttractionDto attraction, MultipartFile file) throws IOException {
		
		attraction.setImg(new FileInfoDto());
		Resource res = resLoader.getResource(attraction.getImg().getSaveFolder());
		if (file != null && file.getSize() > 0) {
			FileInfoDto fileInfo = new FileInfoDto();
			String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			fileInfo.setSaveFile("http://localhost:8080/img/" + name);
			fileInfo.setOriginalFile(file.getOriginalFilename());
			
			attraction.setImg(fileInfo);

			file.transferTo(new File(res.getFile().getCanonicalPath() + "/" + name));
		}
	}
	
	@Override
	@Transactional
	public int regist(AttractionDto attraction, MultipartFile file) throws IOException {
		fileHandling(attraction, file);
		return repo.regist(attraction);
	}

	@Override
	public List<AttractionDto> search(SearchCondition search) {
		return repo.search(search);
	}

	@Override
	public AttractionDto info(int attractionKey) {
		return repo.info(attractionKey);
	}

	@Override
	public List<AttractionTypeDto> searchType() {
		return repo.searchType();
	}

	@Override
	public int getTotalListCount(SearchCondition search) {
		return repo.getTotalListCount(search);
	}

	@Override
	@Transactional
	public AttractionDto modify(AttractionDto attraction) {
		repo.modify(attraction);
		return repo.info(attraction.getAttractionKey());
	}

	@Override
	@Transactional
	public int delete(int attractionKey) {
		return repo.delete(attractionKey);
	}

}
