## 在线日志
---

### 参考
- [Java WebSocket + tail命令实现Web实时日志](https://xxgblog.com/2015/11/25/java-websocket-tail/)
- [springboot websocket简单入门](https://segmentfault.com/a/1190000016012270)

### 部署
```bash
mvn clean package

java -jar  -Dcmd.tail.path=日志文件绝对路径 target/websocket-tail-1.0.0.jar

```

浏览器访问 http://localhost:8765/ 即可