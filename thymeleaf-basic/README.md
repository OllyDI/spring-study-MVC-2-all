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