package com.liwei.ruiyi.config;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(DataSource dataSource,
                          UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

    /** 1. 用于存取 remember-me Token 的表 */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


    // 在 SecurityConfig 中暴露 AuthenticationManager：
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 定义 remember-me 服务：
    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
            UserDetailsService uds,
            PersistentTokenRepository tokenRepo) {
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices(
                        "你的随机key",
                        uds,
                        tokenRepo
                );
        service.setCookieName("remember-me");
        service.setParameter("remember-me");
        service.setTokenValiditySeconds(7 * 24 * 3600);
        return service;
    }

    // 密码策略：由于前端已 MD5，加 DB 存 MD5，则可用 NoOp
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /** 3. 安全过滤链配置 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login.jsp", "/static/**", "/login/userLogin").permitAll()
                        .anyRequest().authenticated()
                )
                // —— 表单登录
                .formLogin(form -> form
                        .loginPage("/login.jsp")
                        .loginProcessingUrl("/login/userLogin")   // 表单 action
                        .defaultSuccessUrl("/home.jsp")
                        .permitAll()
                )
                // —— “记住我” 自动登录
                .rememberMe(rm -> rm
                        .userDetailsService(userDetailsService)
                        .tokenRepository(tokenRepository())
                        .rememberMeCookieName("remember-me")
                        .tokenValiditySeconds(7 * 24 * 3600)  // 7 天
                        .key("CHANGE_THIS_TO_A_RANDOM_SECRET")
                )
                // —— 注销
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.jsp?logout")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                )
        // —— CSRF、Session 固定防护等都是开箱即用
        ;
        return http.build();
    }
}
