package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author d7
 * @see [Java WebSocket + tail命令实现Web实时日志](https://github.com/wucao/websocket-tail-demo)
 */
@Slf4j
@Component
public class LogWebSocketHandle extends TextWebSocketHandler {

    @Value("${cmd: tail -f a.log}")
    private String command;

    private Process process;
    private InputStream inputStream;

    private void onClose() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        if (process != null) {
            process.destroy();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(">>> afterConnectionEstablished session {}", session.getId());

        try {
            // 执行tail -f命令
            log.info(">>> 执行tail -f命令[{}]", command);
            process = Runtime.getRuntime().exec(command);
            inputStream = process.getInputStream();

            // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
            TailLogThread thread = new TailLogThread(inputStream, session);
            thread.start();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info(">>> afterConnectionClosed session {}, closeStatus {}", session.getId(), closeStatus);
        onClose();
    }

}
