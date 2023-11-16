package com.ssafy.triptape.attraction;

import com.ssafy.triptape.file.FileInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관광지 타입 대한 DTO입니다.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "관광지 타입 정보")
public class AttractionTypeDto {
	@ApiModelProperty(value = "관광타입 코드")
	private int typeCode;
	@ApiModelProperty(value = "여행지 타입 이름")
	private String typeName;
}
