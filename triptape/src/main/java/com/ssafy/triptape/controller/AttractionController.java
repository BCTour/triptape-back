package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.service.AttractionService;
import com.ssafy.triptape.common.util.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/attraction")
@Api(tags="관광지 정보 관리 API")
public class AttractionController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AttractionService service;
	
	@PostMapping(value ="/regist", consumes = "multipart/form-data") 
	@ApiOperation("관광지를 등록합니다.")
	public ResponseEntity<?> regist(@ApiParam(value = "관광지 정보", required = true) @RequestPart(value="attraction") AttractionDto attraction, 
			@RequestPart String userId,
			@RequestPart(name="file", required = false) MultipartFile file,
			HttpServletRequest request) throws IllegalStateException, IOException {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
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

			if(search.getLatitude() == 0.0) search.setLatitude(37.501328668708); 	
			if(search.getLongitude() == 0.0) search.setLongitude(127.03953821497);
			List<AttractionDto> list = service.search(search);
			if(list != null) {
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
	
	
	@PutMapping(value="/modify",consumes = "multipart/form-data")
	@ApiOperation(value="관광지 상세 정보를 수정합니다.")
	public ResponseEntity<?> modify(@ApiParam(value = "관광지 정보", required = true) @RequestPart(value="attraction") AttractionDto attraction,
									@RequestPart(name="file", required = false) MultipartFile file){
	
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			attraction.setAttractionKey(attraction.getAttractionKey());
			int result = service.modify(attraction, file);
			if(result == 1) {
				AttractionDto ret = service.info(attraction.getAttractionKey());				
				resultMap.put("attraction", ret);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "수정한 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
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
	
	
	@PostMapping("/like")
	@ApiOperation("관광지 정보 좋아요!")
	public ResponseEntity<?> likeAttraction(@RequestParam int attractionKey, @RequestParam String userId ){

		try {
			service.likeAttraction(attractionKey, userId);;
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping("/dislike")
	@ApiOperation("관광지 정보 좋아요 취소!")
	public ResponseEntity<?> dislikeAttraction(@RequestParam int attractionKey, @RequestParam String userId){

		try {
			service.dislikeAttraction(attractionKey, userId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
