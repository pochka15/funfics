package com.pochka15.funfics.config.websocket;

import com.pochka15.funfics.service.JwtService;
import com.pochka15.funfics.service.users.DbUserDetailsService;
import com.pochka15.funfics.utils.http.HeadersUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Value("${front.url}")
    private String frontOrigin;

    public WebSocketConfig(DbUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sock");
        config.setApplicationDestinationPrefixes("/sock");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/comments-websocket")
                .setAllowedOrigins(frontOrigin)
                .withSockJS();
    }


    /**
     * Authenticate user for the websocket session when the 'Authorization' header is given in the 'CONNECT' message
     *
     * @param registration - injected by spring by default
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                Optional.ofNullable(MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class))
                        .ifPresent(accessor -> {
                            if (StompCommand.CONNECT.equals(accessor.getCommand())) tryToSetJwtUser(accessor);
                        });
                return message;
            }
        });
    }

    private void tryToSetJwtUser(StompHeaderAccessor accessor) {
        final List<String> authHeaderValues = accessor.getNativeHeader("Authorization");
        if (authHeaderValues != null) {
            buildUserDetails(authHeaderValues.get(0))
                    .flatMap(this::buildAuthToken)
                    .ifPresent(accessor::setUser);
        }
    }

    private Optional<UserDetails> buildUserDetails(String authHeaderValue) {
        return Optional.of(authHeaderValue)
                .map(HeadersUtils::extractToken)
                .map(jwtService::extractUsername)
                .map(userDetailsService::loadUserByUsername);
    }

    private Optional<Authentication> buildAuthToken(UserDetails userDetails) {
        return Optional.of(userDetails)
                .map(details -> new UsernamePasswordAuthenticationToken(
                        details, null,
                        details.getAuthorities()));
    }
}
