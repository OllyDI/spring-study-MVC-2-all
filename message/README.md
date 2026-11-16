# 스프링-MVC-2-복습

>26.11.14

## 메시지, 국제화

html에 상품명, 상품이름 등을 고쳐야되는 상황이 생기면 모든 html을 다 수정해야함.

위와 같이 다양한 메시지를 한곳에서 관리하는 기능 - 메시지(message.properties)

메시지(message.properties)파일을 나라 별로 관리 - 국제화(message_en.properties, message_ko.properties)

<br>

### 스프링 메시지 소스 설정

[application.properties](src/main/resources/application.properties) `spring.messages.basename=messages,config.i18n.messages` 스프링 부트가 자동으로 등록
- 별도로 설정하지 않으면 `spring.messages.basename=messages` 하나만 자동 인식

[messages.properties](src/main/resources/messages.properties), [messages_en.properties](src/main/resources/messages_en.properties) 메시지 파일 생성

<br>

### 스프링 메시지 소스 사용

[MessageSourceTest](src/test/java/hello/itemservice/message/MessageSourceTest.java) 메시지 테스트 코드
- line 22 `getMessage(code, args, lacale)` 세개의 값을 넣을 수 있음
- line 28 메시지 코드가 없으면 `NoSuchMessageException` 예외 발생
- line 35 `getMessage(code, args, defaultMessage, lacale)` 기본 메시지를 주면 메시지 코드가 없어도 에러X
- line 40 argument 사용 방식
- line 46 - 51 국제화 locale을 설정해주면 됨 locale 값이 없으면 기본 메시지를 불러옴

<br>

### 웹 애플리케이션에 메시지 적용

타임리프 메시지 표현식 `#{...}` 스프링 메시지를 조회할 수 있음

[MessageItemController](src/main/java/hello/itemservice/web/message/MessageItemController.java), [html 코드](src/main/resources/templates/message) 기존 타임리프.html에 메시지 표현식 적용. 컨트롤러는 동일함

*참고 - 파라미터
- `hello.name=안녕 {0}`
- `<p th:text="#{hello.name(${item.itemName})}"></p>`

<br>

### 웹 애플리케이션에 국제화 적용

[messages_en.properties](src/main/resources/messages_en.properties) 영어 메시지 파일 생성
- 이미 html 메시지 설정이 끝났기 때문에 메시지 파일만 생성하면 끝남
- 클라이언트의 `Accept-Languge`에 따라 메시지 설정이 달라짐