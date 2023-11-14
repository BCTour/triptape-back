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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionComment;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.service.AttractionCommentService;
import com.ssafy.triptape.attraction.service.AttractionService;
import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/attraction/comment")
@Api(tags="관광지 코멘트 관리 API")
public class AttractionCommentController {

	
	@Autowired
	private AttractionCommentService service;
	
	@PostMapping(value ="/regist") 
	@ApiOperation("관광지 코멘트를 등록합니다.")
	public ResponseEntity<?> regist(@RequestBody AttractionComment comment) {		
		
		System.out.println(comment);
		int result = service.regist(comment);
		
		ApiResponse<Object> ar = ApiResponse.builder()
					.result(result).resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
					.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
					.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	 
	@GetMapping("/search/{attractionKey}") 
	@ApiOperation("해당하는 코멘트 정보를 반환합니다.")
	public ResponseEntity<?> search(@PathVariable int attractionKey,@RequestParam int currentPage){
		int totalCount = service.getTotalListCount(attractionKey);
		
		int countPerPage = 10;
		int start = (currentPage - 1) * countPerPage;
		List<AttractionComment> comment = service.search(attractionKey, start, countPerPage);
		
		Map<String, Object> map = new HashMap<>();
		map.put("comment", comment);
		map.put("totalCount", totalCount);
		
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(map).resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
				.build();
	
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}

	
	@PutMapping("/modify/{commentKey}")
	@ApiOperation("코멘트를 수정합니다.")
	public ResponseEntity<?> modify(@PathVariable int commentKey,@RequestBody AttractionComment comment ){

		comment.setCommentKey(commentKey);
		int result = service.modify(comment);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(result).resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
				.resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{commentKey}")
	@ApiOperation("코멘트 정보를 삭제합니다.")
	public ResponseEntity<?> delete(@PathVariable int commentKey){

		int result = service.delete(commentKey);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(result).resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
				.resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
}
