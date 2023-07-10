package xyz.dlice.five.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import xyz.dlice.five.WebSocket.blocker.ChatInterceptor;
import xyz.dlice.five.WebSocket.handler.TextMessageHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getHandler(),"/websocket/*").addInterceptors(new ChatInterceptor()).setAllowedOrigins("*");
    }

    @Bean
    public TextMessageHandler getHandler(){
        return new TextMessageHandler();
    }
}
