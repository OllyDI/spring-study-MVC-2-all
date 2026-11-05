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

<br>

> 26.11.05

### 속성 값 설정

[attribute.html](src/main/resources/templates/basic/attribute.html) 코드

`th:*`: 기존 속성을 `th:*` 대체, 기존 속성이 없으면 새로 생성
- line 10 `th:attrappend`: 속성 값의 앞에 추가
- line 10 `th:attrprepend`: 속성 값의 뒤에 추가
- line 10 `th:classappend`: 클래스 속성에 추가

기존 HTML에서는 `checked="false"` 해도 체크가 됨 `th:checked="false"` 하면 `checked` 속성 자체를 제거

<br>

### 반복 
[each.html](src/main/resources/templates/basic/each.html) 코드 참고

<br>

### 조건부 평가

[condition.html](src/main/resources/templates/basic/condition.html) 코드 참고 - if, unless(if문 반대)

<br>

### 주석
HTML 주석: `<!-- ... -->`

타임리프 파서 주석: `<!--/* ... */-->`, `<!--/*--> ... <!--*/-->` -> 아에 안보임

타임리프 프로토타입 주석 `<!--/*/ ... /*/-->` -> 타임리프로 랜더링시에만 보임


<br>

### 블록 - 타임리프 자체 태그

[block.html](src/main/resources/templates/basic/block.html) 코드

line 8 ~ 16 `<th:block />` 블록 태그 안에 코드를 여러번 반복할 때 사용 - each 만큼 반복
- 타임리트 특성상 HTML 태그안에 속성으로 사용하기 때문에 each만으로 해결하기 어려운 경우 사용

<br>

### 자바스크립트 인라인

[javascript.html](src/main/resources/templates/basic/javascript.html) 코드

line 8 ~ 17 `th:inline="javascript"` 사용X -> 문자열 변환, 내추럴 템플릿, 객체 사용 등 여러 문제 발생
- line 19 텍스트 렌더링 문제 해결
- line 22 자바스크립트 내추럴 템플릿 기능 제공
- line 24 객체를 JSON으로 자동 변환
- 자바스크립트 each는 `[# th:each] [/]` 사용

<br>

### 템플릿 조각

상하단 또는 좌우측의 공통 영역 개발시 타임리프에서 템플릿 조각, 레아아웃 기능 제공

line 4 ~ 9 [footer.html](src/main/resources/templates/template/fragment/footer.html) `th:fragment="..."`템플릿 생성

[fragmentMain.html](src/main/resources/templates/template/fragment/fragmentMain.html) 코드
- line 11 `th:insert="~{}"` insert는 현재 태그 안에 템플릿 추가
- line 15 `th:replace="~{}"` replace는 현재 태그를 템플릿으로 바꿈
- line 18 (`th:replace=""`) 단순히 경로만 존재할 경우 `~{}` 없이 넣을 수 있음
- line 21 마지막에 괄호를 통해 파라미터 전달 가능

<br>

### 템플릿 레이아웃 1

템플릿 조각과 다르게 아에 코드를 넘겨서 사용도 가능

line 5 [layoutMain.html](src/main/resources/templates/template/layout/layoutMain.html) `common_header(~{::title},~{::link})` title, link에 line 6 ~ 8 코드를 전달
- line 2 [base.html](src/main/resources/templates/template/layout/base.html) `th:fragment`로 전달한 title, link를 가져옴
- line 3 [base.html](src/main/resources/templates/template/layout/base.html) 전달한 title로 변경
- line 9 [base.html](src/main/resources/templates/template/layout/base.html) 전달한 두개의 link를 넣음

<br>

### 템플릿 레이아웃 2

`<head>`만 적용하는게 아니라 `<html>` 전체를 적용할 수 있음

line 2 [layoutExtendMain.html](src/main/resources/templates/template/layoutExtend/layoutExtendMain.html) title, section을 전달
- line 2 [layoutFile.html](src/main/resources/templates/template/layoutExtend/layoutFile.html) html 태그에 `th:fragment`를 넣어 전체를 레이아웃으로 교체 가능
- line 4 [layoutFile.html](src/main/resources/templates/template/layoutExtend/layoutFile.html) 전달한 title로 변경
- line 8 ~ 10 [layoutFile.html](src/main/resources/templates/template/layoutExtend/layoutFile.html) 전달한 section으로 변경