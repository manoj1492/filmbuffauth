package com.mycompany.filmbuffauth.config;

import java.util.Collections;

import javax.crypto.SecretKey;

import com.mycompany.filmbuffauth.filters.JwtTokenVerifier;
import com.mycompany.filmbuffauth.service.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public SecurityConfig(ApplicationUserService applicationUserService, 
                                        PasswordEncoder passwordEncoder,
                                        JwtConfig jwtConfig,
                                        SecretKey secretKey){
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{

        http.cors();//.configurationSource(corsConfigurationSource()); // To allow requests from different origins, by default uses a Bean by the name of corsConfigurationSource
        http.csrf().disable(); // To allow POST requests from POSTMAN. Disable CSRF (cross site request forgery)

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        //filter to see if the user token is valid
        http.addFilterBefore(new JwtTokenVerifier(jwtConfig, secretKey), UsernamePasswordAuthenticationFilter.class);

        // Permit access to requests according to pattern and authorization.
        http.authorizeRequests()
            .antMatchers("/**/login").permitAll()
            .antMatchers("/**/websocket/**").permitAll()
            .antMatchers("/**/v1/**").hasAuthority("QUIZ_READ");
        
        // Any other request should be authenticated. Order of these configurations is important.
        http.authorizeRequests().anyRequest().authenticated();

        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /* @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));// Allowed all origins
        configuration.setAllowedMethods(Arrays.asList("*"));// Allowed access to all HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*"));// Allowed every type of header
        configuration.setAllowCredentials(true);// If removed, cannot access /websocket api
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    } */

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow eureka client to be accessed without authentication
        web.ignoring()
                .antMatchers("/eureka/**")
                .antMatchers("/**/login")
                .antMatchers(HttpMethod.OPTIONS, "/**"); // Request type options should be allowed.
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;
    }

}
