package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.service.AttractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/attraction")
@Api(tags="관광지 정보 관리 API")
public class AttractionController {

	
	@Autowired
	private AttractionService service;
	
	@PostMapping(value ="/regist", consumes = "multipart/form-data") 
	@ApiOperation("관광지를 등록합니다.")
	public ResponseEntity<?> regist(@Validated @ApiParam(value = "관광지 정보", required = true) @RequestPart(value="attraction") AttractionDto attraction, @RequestPart(name="file", required = false) MultipartFile file) throws IllegalStateException, IOException {
		
		try {
			int result = service.regist(attraction, file);
			if(result==1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	 
	@GetMapping("/search") 
	@ApiOperation("조건에 해당하는 관광지 정보를 반환합니다.")
	public ResponseEntity<?> search(SearchCondition search){
		Map<String, Object> result = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			int totalCount = service.getTotalListCount(search);

			List<AttractionDto> list = service.search(search);
			
			if(list != null) {
				quickSort(list, 0, list.size() - 1, search.getLatitude(), search.getLongitude());
				
				result.put("attraction", list);
				result.put("totalCount", totalCount);
				
				status = HttpStatus.OK;
			} else {
				result.put("message", "반환할 데이터가 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			result.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(result, status);
	}
	
	
	@GetMapping("/info/{attractionKey}")
	@ApiOperation("관광지 상세 정보를 반환합니다.")
	public ResponseEntity<?> info(@PathVariable int attractionKey ){

		String result;
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			AttractionDto attraction = service.info(attractionKey);
			
			if(attraction != null) {
				return new ResponseEntity<AttractionDto>(attraction, HttpStatus.OK);
			} else { 
				result = "반환할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			result = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(result, status);
		
	}
	
	
	@PutMapping("/modify/{attractionKey}")
	@ApiOperation("관광지 상세 정보를 수정합니다.")
	public ResponseEntity<?> modify(@PathVariable int attractionKey,@RequestBody AttractionDto attraction ){
	
		try {
			attraction.setAttractionKey(attractionKey);
			AttractionDto ret = service.modify(attraction);
			return new ResponseEntity<AttractionDto>(ret, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{attractionKey}")
	@ApiOperation("관광지 정보를 삭제합니다.")
	public ResponseEntity<?> delete(@PathVariable int attractionKey){

		try {
			int result = service.delete(attractionKey);
			if(result==1) return new ResponseEntity<Void>(HttpStatus.OK);
			else return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	private void quickSort(List<AttractionDto> list, int s, int e, double userX, double userY) {
		if(s < e) {
			int p = partition(list, s, e, userX, userY);
			quickSort(list, s, p - 1, userX, userY);
			quickSort(list, p + 1, e, userX, userY);
		}
	}
	
	private int partition(List<AttractionDto> list, int s, int e, double userX, double userY) {
		double p = calc(userX, list.get(s).getLatitude(), userY, list.get(s).getLongitude());
		int l = s + 1;
		int r = e;
		
		do {
			while(l < e && calc(userX, list.get(l).getLatitude(), userY, list.get(l).getLongitude())< p) l++;
			while(r > s && calc(userX, list.get(r).getLatitude(), userY, list.get(r).getLongitude())>= p) r--;
			if(l < r) {
				AttractionDto temp = list.get(l);
	            list.set(l, list.get(r));
	            list.set(r, temp);
			}
		} while(l < r);
		AttractionDto temp = list.get(s);
        list.set(s, list.get(r));
        list.set(r, temp);
		
		return r;
	}
	
	private double calc(double x, double x2, double y, double y2) {
		return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
	}
}
