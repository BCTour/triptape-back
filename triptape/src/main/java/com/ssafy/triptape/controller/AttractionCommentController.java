package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.ssafy.triptape.common.util.JWTUtil;
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
	JWTUtil jwtUtil;
	
	@Autowired
	private AttractionCommentService service;
	
	@PostMapping(value ="/regist") 
	@ApiOperation("관광지 코멘트를 등록합니다.")
	public ResponseEntity<?> regist(
			@RequestBody AttractionComment comment,
			HttpServletRequest request){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		if(!jwtUtil.getUserId(token).equals(comment.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}	
		
		try {
			int result = service.regist(comment);
			if(result==1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	 
	@GetMapping("/search/{attractionKey}") 
	@ApiOperation("해당하는 코멘트 정보를 반환합니다.")
	public ResponseEntity<?> search(@PathVariable int attractionKey,@RequestParam int currentPage){
		int totalCount = service.getTotalListCount(attractionKey);
		
		int countPerPage = 10;
		int start = (currentPage - 1) * countPerPage;
		
		Map<String, Object> result = new HashMap<>();
		
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			List<AttractionComment> comment = service.search(attractionKey, start, countPerPage);
			
			result.put("comment", comment);
			result.put("totalCount", totalCount);
			
			if(comment != null) {
				status = HttpStatus.OK;
			} else { 
				result.put("message", "반환할 데이터가 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			result.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String,Object>>(result, status);
	}

	
	@PutMapping("/modify")
	@ApiOperation("코멘트를 수정합니다.")
	public ResponseEntity<?> modify(
			@RequestBody AttractionComment comment,
			HttpServletRequest request){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		if(!jwtUtil.getUserId(token).equals(comment.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}	

		AttractionComment info = service.commentInfo(comment.getCommentKey());

		if(info == null) {
			resultMap.put("message", "존재하지 않는 댓글입니다.");
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(jwtUtil.getRole(token) == 0 && !comment.getUser().getUserId().equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		try {
			int result = service.modify(comment);
			if(result==1) {
				resultMap.put("comment", service.commentInfo(comment.getCommentKey()));
				status = HttpStatus.OK;
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			else {
				resultMap.put("message", "수정한 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@DeleteMapping("/delete/{commentKey}/{userId}")
	@ApiOperation("코멘트 정보를 삭제합니다.")
	public ResponseEntity<?> delete(
			@PathVariable int commentKey,
			@PathVariable String userId,
			HttpServletRequest request){
		
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

		AttractionComment info = service.commentInfo(commentKey);

		if(info == null) {
			resultMap.put("message", "존재하지 않는 댓글입니다.");
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(jwtUtil.getRole(token) == 0 && !userId.equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		try {
			int result = service.delete(commentKey);
			if(result==1) return new ResponseEntity<Void>(HttpStatus.OK);
			else return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
}
