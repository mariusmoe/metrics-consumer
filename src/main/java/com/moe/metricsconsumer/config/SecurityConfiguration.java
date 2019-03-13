package com.moe.metricsconsumer.config;


import com.moe.metricsconsumer.apiErrorHandling.AlwaysSendUnauthorized401AuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableOAuth2Sso
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/static/**", "/resource/**");
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();

    // READ THIS to fix "always redirect unauthenticated requests"
    // https://stackoverflow.com/questions/4269686/spring-security-need-403-error-not-redirect/30935622
    http.exceptionHandling().authenticationEntryPoint(new AlwaysSendUnauthorized401AuthenticationEntryPoint());

    // Now that errors are handled with 401 they should behave like this
    // https://stackoverflow.com/questions/44692781/configure-spring-boot-to-redirect-404-to-a-single-page-app


    http
      .csrf().disable()
//      Secure all routes except some (more strict)
//      .authorizeRequests()
//      .antMatchers("/","/*.js","/*.js.map","/index.html", "/login", "/static/**").permitAll()
//      .anyRequest().authenticated()

      // Secure only /api/** routes (less strict)
      .authorizeRequests().antMatchers("/api/**").authenticated()

      .and().logout()
      .logoutSuccessUrl("/user/login")
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
      .deleteCookies("JSESSIONID").invalidateHttpSession(false);


//     Should redirect onlogoutSuccess to bellow, but then cors and csrf has to be fixed!
//    https://auth.dataporten.no/logout

  }
}
