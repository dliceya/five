package xyz.dlice.five.WebSocket.blocker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * websocket握手的拦截器，检查握手请求和响应，对WebSocketHandler传递属性
 */
@Slf4j
public class ChatInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String url = request.getURI().toString();
        // 从连接请求中获取连接用户名
        String name = url.substring(url.lastIndexOf("/") + 1);

        attributes.put("name", name);
        log.info("用户：{} 请求连接服务器", name);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
