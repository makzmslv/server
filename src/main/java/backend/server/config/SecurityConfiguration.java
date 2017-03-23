package backend.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalAuthenticationConfigurerAdapter
{

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return new LoginAuthenticationImpl();
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder()
    {
        return new ShaPasswordEncoder();
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/").permitAll().antMatchers("/resources/**").permitAll().antMatchers("/").hasAnyRole("READ_ONLY", "ACCESS_ALL")
                .anyRequest().authenticated().and().httpBasic();
        http.addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
