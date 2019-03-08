package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author d7
 * @see [Java WebSocket + tail命令实现Web实时日志](https://github.com/wucao/websocket-tail-demo)
 */
@Slf4j
public class TailLogThread extends Thread {

    private BufferedReader reader;
    private WebSocketSession session;

    public TailLogThread(InputStream in, WebSocketSession session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;

    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                session.sendMessage(new TextMessage(line + "<br/>"));
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }
}