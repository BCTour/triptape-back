package com.ssafy.triptape.tape;

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
 * Tape에 대한 DTO 입니다.
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="Tape에 대한 정보입니다.")
public class TapeDto {
	
	@ApiModelProperty("테이프키")
	private int tape_key;
	@ApiModelProperty("제목")
	private String title;
	@ApiModelProperty("설명")
	private String description;
	@ApiModelProperty("이미지")
	private FileInfoDto img;
	@ApiModelProperty("사용자")
	private UserDto user;
	@ApiModelProperty("등록일자")
	private String createtime;
	@ApiModelProperty("참여하는 사람 수")
	private int join_num;
	@ApiModelProperty("조회하는 사람 수")
	private int view_num;
}
