package com.etstur.fileapi.config;

import com.etstur.fileapi.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtFilter authenticationJwtTokenFilter() {
    return new JwtFilter();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public FilterRegistrationBean etsTourAuthenticationFilter() throws Exception {
    final var registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(authenticationJwtTokenFilter());
    registrationBean.addUrlPatterns("/files/*");

    return registrationBean;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api-docs/**")
        .permitAll()
        .antMatchers("/actuator/**")
        .permitAll()
        .antMatchers("/users/**")
        .permitAll()
        .antMatchers("/h2-console/**")
        .permitAll()
        .anyRequest()
        .authenticated();

    http.addFilterBefore(
        etsTourAuthenticationFilter().getFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
