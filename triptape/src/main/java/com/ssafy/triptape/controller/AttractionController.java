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
import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;

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
		int result = service.regist(attraction, file);
		
		ApiResponse<Object> ar = ApiResponse.builder()
					.result(result).resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
					.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
					.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	 
	@GetMapping("/search") 
	@ApiOperation("조건에 해당하는 관광지 정보를 반환합니다.")
	public ResponseEntity<?> search(SearchCondition search){
		int totalCount = service.getTotalListCount(search);

		List<AttractionDto> list = service.search(search);
		quickSort(list, 0, list.size() - 1, search.getLatitude(), search.getLongitude());
		
		Map<String, Object> map = new HashMap<>();
		map.put("attraction", list);
		map.put("totalCount", totalCount);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(map).resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	
	@GetMapping("/info/{attractionKey}")
	@ApiOperation("관광지 상세 정보를 반환합니다.")
	public ResponseEntity<?> info(@PathVariable int attractionKey ){

		AttractionDto attraction = service.info(attractionKey);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(attraction).resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	
	@PutMapping("/modify/{attractionKey}")
	@ApiOperation("관광지 상세 정보를 수정합니다.")
	public ResponseEntity<?> modify(@PathVariable int attractionKey,@RequestBody AttractionDto attraction ){

		attraction.setAttractionKey(attractionKey);
		AttractionDto ret = service.modify(attraction);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(ret).resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
				.resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{attractionKey}")
	@ApiOperation("관광지 정보를 삭제합니다.")
	public ResponseEntity<?> delete(@PathVariable int attractionKey){

		int result = service.delete(attractionKey);
		
		if(result != 1) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(result).resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
				.resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
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
