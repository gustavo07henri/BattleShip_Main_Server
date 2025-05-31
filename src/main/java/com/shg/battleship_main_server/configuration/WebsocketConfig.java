package com.shg.battleship_main_server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    @Qualifier("customMessageBrokerTaskScheduler")
    private ThreadPoolTaskScheduler customMessageBrokerTaskScheduler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topics", "/queue")
                        .setHeartbeatValue(new long[] {4000, 4000})
                        .setTaskScheduler(customMessageBrokerTaskScheduler);
        registry.setApplicationDestinationPrefixes("/game");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/battleship-main-server")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new MappingJackson2MessageConverter());
        return false;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry){
        registry.setSendTimeLimit(15 * 1000)
                .setSendBufferSizeLimit(512 * 1024)
                .setMessageSizeLimit(128* 1024);
    }
}
