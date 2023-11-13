package com.ssafy.triptape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final String[] allowedUrls = {
			/* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",			
			"/", "/swagger-ui/**", "/v3/**", 
			/* custom */
			"/user/**"};
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // swagger API 호출시 403 에러 발생 방지
                .authorizeRequests()
                .antMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated();
    }
	
}

