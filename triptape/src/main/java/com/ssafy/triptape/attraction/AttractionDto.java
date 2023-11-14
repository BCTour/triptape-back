package com.ssafy.triptape.attraction;

import com.ssafy.triptape.file.FileInfoDto;

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
public class AttractionDto {
	private int attractionKey;
	private AttractionTypeDto attractionType;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private String description;
	private FileInfoDto img;
}
