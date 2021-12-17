package com.bobsPlus.config;

import com.bobsPlus.service.CustomOAuth2UserService;
import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity // 임시
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests() // 시큐리티 처리에 HttpServletRequest 이용
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight
                    .antMatchers("/oauth2/authorization/**").permitAll() // 특정 경로 허용
                    .antMatchers("/login/oauth2/code/**").permitAll() // 특정 경로 허용
                    .antMatchers("/bobs/room/debug_random").permitAll()
                    .antMatchers("/token/**").permitAll() // 특정 경로 허용
                    .anyRequest().authenticated() // 어떠한 요청이든지 인증되어야 한다 (인증이 안되어 있으면 loginPage 로 이동하겠지?)
                .and()
                    .oauth2Login()
                    .loginPage("/token/error") // 인증되지 않은 사용자는 해당 URL로 리다이렉트 됨
                    .successHandler(successHandler) // oauth2Login 후에 실행되는 핸들러 설정
                    .userInfoEndpoint().userService(oAuth2UserService);
                http.addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }


    //https://howtolivelikehuman.tistory.com/191
    //https://taes-k.github.io/2019/12/05/spring-cors/ (추가필요해보임)
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 추후 https://42bobs.netlify.app 로 변경 필요
        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "PATCH"));
      //configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
      //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
