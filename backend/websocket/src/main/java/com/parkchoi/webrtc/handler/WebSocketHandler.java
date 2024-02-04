package com.parkchoi.webrtc.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    // 사용자가 웹소켓 서버에 접속 하면 동작, CLIENTS 변수에 세션 값 저장
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("연결 시작");
        CLIENTS.put(session.getId(), session);
    }

    // 웹소켓 서버 접속이 끝나면 동작, 세션 제거
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("연결 종료");
        CLIENTS.remove(session.getId());
    }

    // 메시지 전송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();  //메시지를 보낸 아이디
        CLIENTS.forEach((key, value) -> {
            if (!key.equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    value.sendMessage(message);
                    log.info("메시지" + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
