package com.ssafy.triptape.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.record.service.RecordService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/record")
@ApiModel("레코드에 대한 API 입니다.")
public class RecordController {

	@Autowired
	private RecordService service;
	
	@PostMapping("/regist")
	@ApiOperation(value="레코드를 등록합니다.", consumes="multipart/form-data")
	public ResponseEntity<?> registRecord(@RequestPart(value="record") RecordDto record, @RequestPart(value="file", required = false) MultipartFile file) {
		
		try {
			int result = service.registRecord(record, file);
			if(result == 1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/attraction")
	@ApiOperation(value="레코드에 포함된 장소들 목록을 조회합니다.")
	public ResponseEntity<?> recordAttraction(@RequestParam int tapeKey, @RequestParam(required=false)String keyword,@RequestParam(required=false) String word, @RequestParam int currentPage) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			int countPerPage = 10;
			int start = (currentPage - 1) * countPerPage;
			List<AttractionDto> attractionList = service.recordAttraction(tapeKey, keyword, word, start, countPerPage);
			if(attractionList == null || attractionList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("attraction", attractionList);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/search/{tapeKey}")
	@ApiOperation(value="레코드에 포함된 장소들 목록을 조회합니다.")
	public ResponseEntity<?> searchRecord(@PathVariable int tapeKey, @RequestParam(required=false)String keyword,@RequestParam(required=false) String word, @RequestParam int currentPage) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			int countPerPage = 10;
			int start = (currentPage - 1) * countPerPage;
			List<RecordDto> attractionList = service.searchRecord(tapeKey, keyword, word, start, countPerPage);
			if(attractionList == null || attractionList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("attraction", attractionList);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	
	@GetMapping("/info/{recordKey}")
	@ApiOperation("특정 레코드 정보를 조회합니다.")
	public ResponseEntity<?> searchInfo(@PathVariable int recordKey){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;

		try {
			RecordDto record = service.recordInfo(recordKey);

			if(record == null) { 
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("record", record);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PutMapping("/modify")
	@ApiOperation(value="특정 레코드를 수정합니다.", consumes="multipart/form-data")
	public ResponseEntity<?> modifyRecord(@RequestPart(value="record") RecordDto record, @RequestPart(value="file", required = false) MultipartFile file){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;

		try {
			int result = service.modifyRecord(record, file);
			if(result == 1) {
				status = HttpStatus.OK;
				RecordDto recordResult = service.recordInfo(record.getRecordKey());
				resultMap.put("record", recordResult);
			}else {
				status = HttpStatus.NO_CONTENT;
				message = "수정할 내용이 없습니다.";
				resultMap.put("message", message);
			}
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = e.getMessage();
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@DeleteMapping("/delete/{recordKey}")
	@ApiOperation("특정 테이프를 삭제합니다.")
	public ResponseEntity<?> deleteRecord(@PathVariable int recordKey){
		
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			int result = service.deleteRecord(recordKey);
			if(result == 1) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			}else {
				status = HttpStatus.NO_CONTENT;
				message = "삭제할 내용이 없습니다.";
				resultMap.put("message", message);
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(message, status);
	}
	
	@PostMapping("/like")
	@ApiOperation("특정 테이프를 관심 리스트에 추가합니다.")
	public ResponseEntity<?> likeRecord(@RequestParam int recordKey, @RequestParam String userId){
		
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			service.likeRecord(recordKey, userId);
			status = HttpStatus.CREATED;
			return new ResponseEntity<>(status);
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
	@DeleteMapping("/dislike")
	@ApiOperation("특정 테이프를 관심 리스트에 삭제합니다.")
	public ResponseEntity<?> dislikeRecord(@RequestParam int recordKey, @RequestParam String userId){
		
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			service.dislikeRecord(recordKey, userId);
			status = HttpStatus.OK;
			return new ResponseEntity<>(status);
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
}
