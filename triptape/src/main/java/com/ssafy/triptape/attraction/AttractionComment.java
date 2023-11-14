package com.ssafy.triptape.attraction;

import com.ssafy.triptape.user.UserDto;

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
public class AttractionComment {
	private int commentKey;
	private UserDto user;
	private int attractionKey;
	private String content;
	private String createtime;

}
