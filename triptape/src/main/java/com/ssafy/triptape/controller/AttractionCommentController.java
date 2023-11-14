package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.List;

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
import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.attraction.SearchCondition;
import com.ssafy.triptape.attraction.service.AttractionCommentService;
import com.ssafy.triptape.attraction.service.AttractionService;
import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;
import com.ssafy.triptape.common.util.PageNavigation;
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
	
	@PostMapping(value ="/regist/{attractionKey}") 
	@ApiOperation("관광지 코멘트를 등록합니다.")
	public ResponseEntity<?> regist(@PathVariable int attractionKey, @RequestPart(value="attraction") AttractionComment comment) {
		comment.getAttraction().setAttractionKey(attractionKey);
		
		int result = service.regist(comment);
		
		ApiResponse<Object> ar = ApiResponse.builder()
					.result(result).resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
					.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
					.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	 
	@GetMapping("/search") 
	@ApiOperation("조건에 해당하는 코멘트 정보를 반환합니다.")
	public ResponseEntity<?> list(int commentKey, int currentPage){
		int totalCount = service.getTotalListCount(commentKey);
		PageNavigation nav = new PageNavigation(currentPage, totalCount);
		
		List<AttractionComment> list = service.search(commentKey);
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(list).resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
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
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(result).resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
				.resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
				.build();
	
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
}
