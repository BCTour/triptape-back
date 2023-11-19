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
 * 관광지 정보에 대한 DTO입니다.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "관광지 정보")
public class AttractionDto {
	@ApiModelProperty(value = "관광지 키")
	private int attractionKey;
	@ApiModelProperty(value = "관광 타입")
	private AttractionTypeDto attractionType;
	@ApiModelProperty(value = "관광지명")
	private String name;
	@ApiModelProperty(value = "관광지 주소")
	private String address;
	@ApiModelProperty(value = "현재 위치(위도)")
	private Double latitude;
	@ApiModelProperty(value = "현재 위치(경도)")
	private Double longitude;
	@ApiModelProperty(value = "설명")
	private String description;
	@ApiModelProperty(value = "관광지 이미지")
	private FileInfoDto img;
	@ApiModelProperty(value ="사용자 아이디")
	private String userId;

	@ApiModelProperty(value="좋아요")
	private int popular;
	@ApiModelProperty(value="신고수")
	private int report;
}
