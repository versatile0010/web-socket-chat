# web-socket-chat
### 💡 goal: web socket chatting server 이해하기!

```
java 11
spring boot 2.7.15
```

---

- ### 1. feat/basic-websocket

  - 간단한 websocket chatting server 구현
  - only 1:1 chat

- ### 2. feat/stomp

    - stomp 으로 채팅 서비스 고도화
    - publish / subscribe
      - 메시지 공급 주체와 소비 주체를 분리한다.
      - 채팅방 생성 : create `topic`
      - 메세지 발송 : do `publish`
      - 메세지 수신 : 애 'subscribe'
    - Why Stomp?
      - pub/sub 구조로 메세지 전송/수신 부분이 명확히 분리되어 있기 때문에 로직 구성이 용이함
      - Header 에 값을 세팅할 수 있고, 이를 통한 인증 처리를 구현하기 좋음

- ### 3. feat/redis

  - 다중 채팅 서버 간 메세지 공유


- ### 4. feat/spring-security

    - 채팅 서버 보안 강화


- ### 5. feat/event

    - 이벤트 처리

- ### 6.  feat/infra

    - Deployment

