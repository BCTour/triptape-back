package com.ssafy.triptape.record;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.user.UserDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * record에 대한 DTO 입니다.
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="Record에 대한 정보입니다.")
public class RecordDto {
	
	@ApiModelProperty("레코드키")
	private int recordKey;
	@ApiModelProperty("테이프키")
	private int tapeKey;
	@ApiModelProperty("내용")
	private String content;
	@ApiModelProperty("이미지")
	private FileInfoDto img;
	@ApiModelProperty("사용자")
	private UserDto user;
	@ApiModelProperty("관광지정보")
	private AttractionDto attraction;
	@ApiModelProperty("등록일자")
	private String createtime;
	@ApiModelProperty("참조레코드키")
	private int parentRecordKey;
}
