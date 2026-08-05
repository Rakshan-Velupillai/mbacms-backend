package com.mbacms.config;


import com.mbacms.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http

                .csrf(AbstractHttpConfigurer::disable)
                    //spring needs this for performing the DML ops(PUT,POST..)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/user-details").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/patient/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/patient/insurance-plans").authenticated()

                        .requestMatchers(HttpMethod.GET,"/api/auth/all-users").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/user-stat").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/auth/soft-delete/{id}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/patient/all").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/invoice/patient-invoices").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/api/invoice/pay/{invoiceId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/patient/verify/{patientCode}").hasAuthority("HEALTHCARE")





                        .requestMatchers(HttpMethod.POST, "/api/auth/admin/signup").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/healthcare/user-profile").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/insurance-company/user-profile").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/claim/add").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/api/medical-service/add").hasAuthority("HEALTHCARE")
                        .requestMatchers(HttpMethod.POST,"/api/invoice/generate").hasAuthority("HEALTHCARE")
                        .requestMatchers(HttpMethod.GET,"/api/invoice/getInvoiceById/").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/invoice/healthcare-invoices/").hasAuthority("HEALTHCARE")

                        .requestMatchers(HttpMethod.GET,"/api/insurance-company/get-profile").hasAuthority("INSURANCE_COMPANY")
                        .requestMatchers(HttpMethod.PATCH,"/api/insurance-company/update-profile").hasAuthority("INSURANCE_COMPANY")


                        .requestMatchers(HttpMethod.GET,"/api/auth/login").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/patient/getPatient").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/healthcare/get-healthcare").hasAuthority("HEALTHCARE")
                        .requestMatchers(HttpMethod.GET,"/api/claim/all").hasAuthority("INSURANCE_COMPANY")
                        .requestMatchers(HttpMethod.GET,"api/claim/getById/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/medical-service/getAll").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/medical-service/getById/{id}").permitAll()
                        .requestMatchers(HttpMethod.PATCH,"/api/auth/change-password").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/patient/patient-update").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.DELETE,"/api/claim/delete/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/claim/update/").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/api/medical-service/update/{id}").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/patient/insurance-plan/select").hasAuthority("PATIENT")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//adding jwt filter
                .httpBasic(Customizer.withDefaults());//telling the spring that we are using basic auth


        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao=new DaoAuthenticationProvider(userService);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
