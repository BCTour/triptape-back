package com.ssafy.triptape.banner;

import com.ssafy.triptape.attraction.AttractionTypeDto;
import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.tape.TapeDto;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "배너 정보")
public class BannerDto {
	private int bannerKey;
	private TapeDto tape;
	private String title;
	private String description;
}
