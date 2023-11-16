package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.service.EmailService;
import com.ssafy.triptape.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@Api(tags="이미지 관리 API")
@Slf4j
public class ImgController {

	@Autowired
	ResourceLoader resLoader;
	
	@GetMapping(value ="/img/{imgName}")
	@ApiOperation("이미지를 불러옵니다.")
	public ResponseEntity<?> encodeImg(@PathVariable String imgName) throws IOException {		
		String saveFolder = "classpath:static/resources/upload";
		Resource res = resLoader.getResource(saveFolder);

		String saveFile = res.getFile().getCanonicalPath() + "/" +imgName;
		
		Base64.Encoder encoder = Base64.getEncoder();
        byte[] photoEncode = encoder.encode(saveFile.getBytes());
        String photoImg = new String(photoEncode, "UTF8");
		
        
        return new ResponseEntity<String>(photoImg, HttpStatus.OK);

	}
}
