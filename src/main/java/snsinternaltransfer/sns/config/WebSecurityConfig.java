package snsinternaltransfer.sns.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import snsinternaltransfer.sns.service.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }
    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // The pages does not require login
        http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();

        // /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
        // If no login, it will redirect to /login page.
        http.authorizeRequests().antMatchers("/userCreate").access("hasRole('ROLE_USER')");

        // For ADMIN only.
        http.authorizeRequests().antMatchers("/adminMenu").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/createItem").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/editItem").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/itemList").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/updateItem/{updated}").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/updateSending/{updated}").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/deleteSending").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/deleteItem").access("hasRole('ROLE_ADMIN')");

        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                // Submit URL of login page.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/")//
                .successHandler(customizeAuthenticationSuccessHandler)
                .failureUrl("/?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");

    }
}
