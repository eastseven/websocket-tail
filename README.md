## 在线日志
---

### 参考
- [Java WebSocket + tail命令实现Web实时日志](https://xxgblog.com/2015/11/25/java-websocket-tail/)
- [springboot websocket简单入门](https://segmentfault.com/a/1190000016012270)
- [nginx websocket简介及配置](http://coolnull.com/4275.html)

### 部署
```bash
mvn clean package

java -jar  -Dcmd.tail.path=日志文件绝对路径 target/websocket-tail-1.0.0.jar

```

浏览器访问 http://localhost:8765/ 即可

### nginx 配置
```bash
upstream ws_server {
  server 127.0.0.1:3000;
}

server {
  listen 80;
  server_name ws.repo;

  location / {
    proxy_pass http://ws_server/;
    proxy_redirect off;

    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
  }
}
```

核心配置就下面这三行
```bash
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
```