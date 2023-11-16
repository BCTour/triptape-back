package com.ssafy.triptape.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileInfoDto {
	private String saveFolder = "classpath:static/resources/upload";
	private String originalFile; // 원본 파일 이름
	private String saveFile; // 실제 저장 파일 이름
	
}
