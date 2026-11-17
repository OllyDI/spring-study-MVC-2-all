# 스프링-MVC-2-복습

>26.11.17

## 검증1 - Validation

상품명, 가격 등 입력 검증이 없어 오류화면으로 넘어가는 문제 발생. 이를 해결하기 위해 검증 로직 추가

- 타입 검증
  - 가격, 수량에 문자가 들어가면 검증 오류 처리
- 필드 검증
  - 상품명: 필수, 공백X
  - 가격: 1000원 이상, 1백만원 이하
  - 수량: 최대 9999
- 특정 필드의 범위를 넘어서는 검증
  - 가격 * 수량의 합은 10,000원 이상

#### * 현재 섹션에서는 검증을 직접 구현, 이후에 타임리프가 제공하는 기능으로 검증 기능 구현

<br>

### 검증 직접 처리 - 개발

[ValidationItemControllerV1](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV1.java) 코드
- line 49 검증 오류 결과 보관 변수
- line 52 ~ 60 상품이름, 가격, 수량 조건에 따라 검증 로직 구현
- line 62 ~ 68 가격 * 수량 요구사항 검증 로직 구현
- line 70 ~ 75 검증 실패시 입력 폼으로 이동 - 입력 폼으로 다시 이동해도 값이 남아있는 이유 line 46 `@ModelAttribute Item item`

[addForm.html](src/main/resources/templates/validation/v1/addForm.html) 에러 출력을 위해 html 수정
- line 26 ~ 28 글로벌 에러 처리 `errors?.containsKey('globalError')` 물음표를 넣으면 null은 무시 -> SpringEL 문법
  - 처음 상품 등록 클릭시에는 `errors` 값이 null -> `NullPointerException 오류 발생. 물음표 처리 필수
- line 34 ~ 35 상품명 에러가 있으면 css 처리를 위해 클래스에 `th:classappend`를 이용 `field-error` 추가 line 35에서 에러 표시 추가
- line 40 ~ 41 가격 에러, line 46 ~ 47 수량 에러 추가 내용은 위와 동일

V1 문제점
1. 뷰 템플릿 중복처리가 많음
2. 가격, 수량의 타입 오류 처리는 안됨 -> 문자 입력시 400 오류 페이지 이동
3. 사용자가 가격, 수량에 문자를 입력했어도 오류 페이지 이동 없이 화면에 남아있어야 함
   - 현재는 Integer 타입이라 문자를 담을 수 없음
4. 결국 고객이 입력한 값을 따로 만들어서 보관해야 함

<br>

### 검증 V2 - BindingResult 1

[ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) 코드

line 60 ~ 94 addItemV1
- line 60 `BindingResult` 추가 - `BindingResult`는 반드시 `@ModelAttribute` 다음에 와야됨
- line 64, 67, 71 `bindingResult.addError(new FieldError(objectName, field, defaultMessage))` 필드 오류 처리
  - objectName: `@ModelAttribute` 이름
  - field: 오류가 발생한 필드 이름
  - defaultMessage: 오류 기본 메시지
- line 78 `bindingResult.addError(new ObjectError(objectName, defaultMessage))` 글로벌 오류는 오브젝트 에러 처리
  - 오브젝트 에러(글로벌 오류)는 필드가 따로 필요 없음

[addForm.html](src/main/resources/templates/validation/v2/addForm.html) V2 코드
- line 26 **글로벌 오류 처리**: `#fields`를 사용하면 `BindingResult` 검증 오류에 접근 가능
- line 27 글로벌 오류가 여러 개일 수 있기 때문에 `th:each="err : ${#fields.globalErrors()}"`로 처리
- line 33 **필드 오류 처리**: `th:errorclass="field-error"`: `th:field`로 지정한 필드에 에러가 있으면 클래스 추가
- line 34 v1에서처럼 복잡하게 할 필요 없이 `th:errors="*{...}"`이면 끝남 -> `th:if` 편의 버전

### 검증 V2 - BindingResult 2

`@ModelAttribute`데이터에 바인딩 오류가 발생해도 `BindingResult` 호출 -> 가격, 수량에 Integer가 아닌 문자 입력시

`BindingResult` 검증 오류 적용 3가지 방법
1. `@ModelAttribute` 객체 오류 등으로 바인딩 실패 -> FieldError 생성 후 `BindingResult`에 넣음
2. 개발자가 직접 넣음 -> V1처럼 직접 구현
3. `Validator`사용 -> 이후 뒤에서 설명

#### `BindingResult`는 반드시 검증할 대상 다음에 와야됨

V2 문제점 
- 입력 오류가 발생하는 경우 사용자가 입력한 값이 모두 사라짐
- 가격, 수량에 문자 입력시 에러 문구 수정이 필요함

<br>

### FieldError, ObjectError

[ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) 코드

목표: 입력 오류가 발생해도 사용자가 입력한 값 보존

line 97 ~ 140 addItemV2
- line 111 `fieldError` 파라미터중 `rejectedValue`에 사용자 입력 값을 넣으면 보존 가능
  * `objectName`: 객체 이름
  * `field`: 오류 필드
  * `rejectedValue`: 사용자 입력 값
  * `bindingFailure`: 타입 오류 같은 바인딩 실패인지 구분 값
  * `codes`: 메시지 코드
  * `arguments`: 메시지에서 사용하는 인자
  * `defaultMessage`: 기본 오류 메시지
- line 125 `objectError` 처리는 전과 동일하게 해도 됨 -> 입력 값 처리 필요가 없음

타임리프 `th:feild`는 정상 동작시에는 모델 객체 값 사용, 오류 발생시 `FieldError`값 사용

가격, 수량과 같은 타입 오류로 바인딩 실패시 `FieldError` 생성. 사용자 입력 값을 넣음 이후 `BindingResult`에 담아서 호출
- 그래서 타입 오류가 발생해도 문자를 입력한 사용자 입력도 정상 출력됨

addItemV2 문제점
- 타입 오류 메시지가 스프링에서 기본적으로 제공하는 메시지임. 수정 필요