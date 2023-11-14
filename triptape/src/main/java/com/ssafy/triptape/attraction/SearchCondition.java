package com.ssafy.triptape.attraction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SearchCondition (관광지 정보 조건)")
public class SearchCondition {

	public final int countPerPage = 10;
	
	@ApiModelProperty("관광타입")
	private int typeCode;
	@ApiModelProperty("관광지명")
	private String name;
	@ApiModelProperty("주소")
	private String address;
	@ApiModelProperty("현재 위치(위도)")
	private double latitude;
	@ApiModelProperty("현재 위치(경도)")
	private double longitude;
	@ApiModelProperty("현재 페이지")
	private int currentPage = 1;

	public int getOffset() {
		return (this.currentPage - 1) * countPerPage;
	}
}

