package springframework.project.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/games").permitAll()
            .antMatchers("/games/join").permitAll()
            .anyRequest().permitAll();

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }
    
}