package com.ssafy.triptape.banner.repo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.banner.BannerDto;

@Mapper
public interface BannerRepo {
	int registBanner(BannerDto banner);
	List<BannerDto> searchBanner();
	int modifyBanner(BannerDto banner);
	int deleteBanner(int bannerKey);
}
