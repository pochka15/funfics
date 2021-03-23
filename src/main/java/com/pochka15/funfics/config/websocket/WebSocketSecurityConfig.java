package com.pochka15.funfics.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


@Configuration
public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/save-comment").authenticated();
    }

    /**
     * Disable CSRF within WebSockets
     *
     * @return true if should disable Same-Origin policy
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}