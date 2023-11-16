package com.ssafy.triptape.attraction;

import com.ssafy.triptape.user.UserDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관광지 댓글에 대한 DTO입니다.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "관광지에 대한 댓글")
public class AttractionComment {
	@ApiModelProperty(value = "장소 댓글 키")
	private int commentKey;
	@ApiModelProperty(value = "장소 댓글 작성자")
	private UserDto user;
	@ApiModelProperty(value = "장소 키")
	private int attractionKey;
	@ApiModelProperty(value = "댓글 내용")
	private String content;
	@ApiModelProperty(value = "등록일자")
	private String createtime;

}
