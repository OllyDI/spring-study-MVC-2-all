# 스프링-MVC-2-복습

>26.11.03

## 타임리프 - 기본 기능

[BasicController](src/main/java/hello/thymeleaf/basic/BasicController.java), [TemplateController](src/main/java/hello/thymeleaf/basic/TemplateController.java) 타임리프에 사용할 컨트롤러

### 텍스트 - text, utext
[text-basic.html](src/main/resources/templates/basic/text-basic.html) 코드

line 11 태그 `th:text`, line 12 직접 출력 `[[${...}]]`

**주의: `<`, `>` 등 타임리프는 HTML 엔티티로 변경해줌 -> 이스케이프(escape) 라고 함**

[unescaped.html](src/main/resources/templates/basic/text-unescaped.html) unescape는 `utext`, `[(...)]` 사용

<br>

### 변수 - SpringEL

[variable.html](src/main/resources/templates/basic/variable.html) 코드

Object - `user.username`, `user['username']`, `user.getUsername()`

List - `users[0].username`, `users[0]['username']`, `users[0].getUsername()`

Map - `userMap['userA'].username`, `userMap['userA']['username']`, `userMap['userA'].getUsername()`

지역 변수 선언

line 25 - 26 `th:with="변수이름"` - 선언한 태그 안에서만 사용 가능

<br>

>26.11.04

### 기본 객체들

[basic-objects.html](src/main/resources/templates/basic/basic-objects.html) 코드

line 10 ~ `${request}`, `${response}`, `${session}` 등 접근 가능

line 18 `${param.paramName}`을 통해 파라미터 바로 접근 가능

line 19 [BasicController](src/main/java/hello/thymeleaf/basic/BasicController.java) line 57 객체를 담은 세션 꺼내서 사용 가능

line 20 [BasicController](src/main/java/hello/thymeleaf/basic/BasicController.java) line 64 스프링 빈에 직접 접근 가능

<br>

### 유틸리티 객체와 날짜

[date.html](src/main/resources/templates/basic/date.html) 코드

[유틸리티 객체](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utility-objects), [유틸리티 객체 예시](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects) 참고

<br>

### 리터럴

[literal.html](src/main/resources/templates/basic/literal.html) 코드

타임리프 문자는 항상 `'...'`작은 따옴표 사용 `<span th:text="'hello'">`

다음과 같은 문자가 공백 없이 쭉 이어진다면 작은 따옴표 생략 가능
- `A-Z`, `a-z`, `0-9`, `[]`, `.`, `-`, `_`

리터럴 대체 - `|hello ${...}|`

<br>

### 연산

[operation.html](src/main/resources/templates/basic/operation.html) 코드

HTML 엔티티 사용만 주의: `>`-`&gt`, `<`-`lt`,  `>=`-`ge`,  `<=`-`le`,  `==`-`eq`,  `!=`-`neq`

line 33 - 34 Elvis 연산자 `${data}?: '...'` 데이터가 없으면 오른쪽 문자열 출력

line 39 - 40 NO-Operation `${data}?: _` 데이터가 없으면 아무것도 하지 않음