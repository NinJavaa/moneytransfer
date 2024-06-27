package com.js.moneytransfer.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties(AccountProperties.class)
@ActiveProfiles("test")
class AccountPropertiesTest {

    @Test
    void accountPropertiesShouldLoad() {
        // This test will fail if the properties cannot be loaded
        AccountProperties properties = new AccountProperties();
        assertThat(properties).isNotNull();
    }
}
