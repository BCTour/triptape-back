package com.ssafy.triptape.banner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.triptape.banner.BannerDto;
import com.ssafy.triptape.banner.repo.BannerRepo;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	BannerRepo repo;
	
	
	@Override
	@Transactional
	public int registBanner(BannerDto banner) {
		return repo.registBanner(banner);
	}

	@Override
	public List<BannerDto> searchBanner() {
		return repo.searchBanner();
	}

	@Override
	@Transactional
	public int modifyBanner(BannerDto banner) {
		return repo.modifyBanner(banner);
	}

	@Override
	@Transactional
	public int deleteBanner(int bannerKey) {
		return repo.deleteBanner(bannerKey);
	}

}
