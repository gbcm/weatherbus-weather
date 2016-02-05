package io.pivotal.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "cloud" })
@EnableEurekaClient
public class CloudConfig {
}
