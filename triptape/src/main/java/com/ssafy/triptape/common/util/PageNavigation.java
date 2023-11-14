package com.ssafy.triptape.common.util;

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
public class PageNavigation {
	
	private boolean startRange; // 현재 페이지가 이전이 눌려지지 않는 범위의 페이지 체크
	private boolean endRange; // 현재 페이지가 다음이 눌려지지 않는 범위의 페이지 체크
	private int startPage;
	private int endPage;
	private int totalCount; // 총 게시글 갯수
	private int newCount; // 새글 갯수
	private int totalPageCount; // 총 페이지 갯수
	private int currentPage; // 현재 페이지 번호
	private int naviSize = 10; // 네비게이션 사이즈
	private int countPerPage = 20; // 페이지당 글 갯수
	private String navigator;

	public PageNavigation(int currentPage, int totalCount) {
		makePageNavigation(currentPage, totalCount);
	}
	
	private void makePageNavigation(int currentPage, int totalCount) {
		// pageNation의 currentPage를 설정한다.
		this.setCurrentPage(currentPage);
		// pageNation의 totalCount를 설정한다.
		this.setTotalCount(totalCount);
		// pageNation의 countPerPage를 설정한다.
		this.setCountPerPage(naviSize);

		// totalPageCount를 계산한다.
		int totalPageCount = (totalCount - 1) / countPerPage + 1;
		// pageNation의 totalPageCount를 설정한다.
		this.setTotalPageCount(totalPageCount);

		// pageNavigation의 시작 페이지를 계산해서 설정한다.
		this.setStartPage((currentPage - 1) / naviSize * naviSize + 1);

		// pageNavigation의 endPage를 계산해서 설정한다.
		this.setEndPage(this.getStartPage() + naviSize - 1);
		// 만약 totalPageCount가 위에서 계산한 값보다 작다면 totalPageCount로 설정한다.

		if (totalPageCount < this.getEndPage()) {
			this.setEndPage(totalPageCount);
		}
		// pageNation의 startRange를 계산해서 설정한다.
		this.setStartRange(currentPage <= naviSize);
		// pageNation의 endRange를 계산해서 설정한다.
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		this.setEndRange(endRange);
	}

}
