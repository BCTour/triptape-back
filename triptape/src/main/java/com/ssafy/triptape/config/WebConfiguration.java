package com.ssafy.triptape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.triptape.common.interceptor.JWTInterceptor;


@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
	
	@Autowired
	private JWTInterceptor jwtInterceptor;

	public WebConfiguration(JWTInterceptor jwtInterceptor) {
		super();
		this.jwtInterceptor = jwtInterceptor;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
//		default 설정.
//		Allow all origins.
//		Allow "simple" methods GET, HEAD and POST.
//		Allow all headers.
//		Set max age to 1800 seconds (30 minutes).
		registry
			.addMapping("/**")
			.allowedOrigins("http://localhost:8080", "http://localhost:5173", "http://localhost:5174")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
			.allowedHeaders("Authorization", "Content-Type")
			.exposedHeaders("*")
			.maxAge(1800); // Pre-flight Caching
	}

//	private static final String[] INTERCEPTOR_WHITE_LIST = {
//			"/swagger-ui/**", "/swagger-resources/**","/v2/api-docs",
//			"/user/login",
//			"/user/regist",
//			"/user/refresh",
//			"/user/findpw",
//			"/user/regist/pw",
//			"/attraction/**",
//			"/img/**",
//			"/record/**",
//			"/tape/**",
//			"/error/**"
//	};
//	
////	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(jwtInterceptor)
//		.excludePathPatterns(INTERCEPTOR_WHITE_LIST);
//	}

//	Swagger UI 실행시 404처리
//	Swagger2 일경우
//	@Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}