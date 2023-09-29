package dev.shvetsova.ewmc.request.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class OAuthFeignConfig {
    private final OAuth2ClientProperties oAuth2ClientProperties;

    public OAuthFeignConfig(OAuth2ClientProperties oAuth2ClientProperties) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Bean
    @ConditionalOnBean({OAuth2AuthorizedClientService.class, ClientRegistrationRepository.class})
    @ConditionalOnMissingBean
    public OAuth2AuthorizedClientManager feignOAuth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
    }

    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientManager.class)
    public OAuth2AccessTokenInterceptor defaultOAuth2AccessTokenInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        return new OAuth2AccessTokenInterceptor(oAuth2ClientProperties.getRegistration().keySet().iterator().next(), oAuth2AuthorizedClientManager);
    }
}
