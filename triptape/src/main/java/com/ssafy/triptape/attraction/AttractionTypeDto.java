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
public class AttractionTypeDto {
	private int typeCode;
	private String typeName;
}
