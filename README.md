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
      - 메세지 수신 : 애 `subscribe`
    - 🟢 Why Stomp?
      - pub/sub 구조로 메세지 전송/수신 부분이 명확히 분리되어 있기 때문에 로직 구성이 용이함
      - Header 에 값을 세팅할 수 있고, 이를 통한 인증 처리를 구현하기 좋음
    - 🔴 But...
      - 채팅 기록을 저장할 storage 가 없기 때문에, 각각의 채팅방은 서버를 재시작할 때마다 대화 기록이 초기화 됨.  
      - publish / subscribe 가 발생한 서버 내에서만 채팅이 가능함. 다른 서버로 접속한 클라이언트는 해당 채팅방의 존재를 알 수 없음.
      - topic 은 단일 서버에 의존하지 않고, 모든 서버에 대하여 열려있어야함.  

- ### 3. feat/redis

    - 🟢 Why Redis?

      - publish / subscribe 를 지원하고 있기 때문에 다중 서버 간 공통 publish / subscribe 채널로 이용하기에 용이함.
      - 각각 다른 서버에 접속해 있는 클라이언트들이 채팅방을 통해 다른 서버의 클라이언트와 메세지를 주고받을 수 있도록 고도화 가능


- ### 4. feat/spring-security

    - 🟢 Spring security 를 통한 채팅 관련 접근 권한 통제

      - 로그인 한 회원만 채팅 화면에 접근가능하도록 설정
      - Web Socket connect , message send 는 jwt 를 통해 Authenticate
      - header 에 jwt token 을 setting


- ### 5. feat/event

    - 추가 요구사항
   
      - 채팅방 입장/퇴장 시 알림 메세지는 서버에서 처리하기
      - 채팅방 입장/퇴장 시 현재 인원 수 표시하기

- ### 6.  feat/infra

    - Deployment

