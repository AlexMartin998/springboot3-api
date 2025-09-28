package com.ecommerce.api.auth.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "app.security.jwt")  // enable in MainClass @EnableConfigurationProperties <- Injectable - main class
public class JwtProperties {

    private String secret;
    private Long expiration; // in hours

}
