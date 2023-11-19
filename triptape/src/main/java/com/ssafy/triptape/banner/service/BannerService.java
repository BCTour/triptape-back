package com.ssafy.triptape.banner.service;

import java.util.List;

import com.ssafy.triptape.banner.BannerDto;

public interface BannerService {
	int registBanner(BannerDto banner);
	List<BannerDto> searchBanner();
	int modifyBanner(BannerDto banner);
	int deleteBanner(int bannerKey);
}
