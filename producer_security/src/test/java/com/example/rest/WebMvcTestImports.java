package com.example.rest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cloud.security.oauth2.client.ResourceServerTokenRelayAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.example.auth.ResourceServerConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ ResourceServerConfiguration.class, ResourceServerTokenServicesConfiguration.class, OAuth2AutoConfiguration.class,
    ResourceServerTokenRelayAutoConfiguration.class, WebMvcAutoConfiguration.class })
// @ActiveProfiles(value = { "integrationtest" })
public @interface WebMvcTestImports {

}
