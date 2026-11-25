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

<br>

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
- line 111 `fieldError` 파라미터 중 `rejectedValue`에 사용자 입력 값을 넣으면 보존 가능
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

<br>

>26.11.20

### 오류 코드와 메시지 처리1

[application.properties](src/main/resources/application.properties) `errors.properties`를 사용하기 위해 `basename`에 errors 추가

[errors.properties](src/main/resources/errors.properties) 에러 메시지 추가

line 144 ~ 180 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) addItemV3 추가 - defaultMessage 제거, codes, arguments 추가
- line 151 ~ 158 필드 에러 메시지(codes) `new String[]{"..."}` 추가
- line 165 오브젝트 에러 메시지(codes, arguments) 추가

<br>

### 오류코드와 메시지 처리2

`FieldError`와 `ObjectError` 너무 번거로움 -> `BindingResult`가 제공하는 `rejectVale()` or `reject()` 사용

`BindingResult`는 검증 대상 바로 뒤에 오기 때문에 이미 검증해야 할 대상을 알고 있음 -> `objectName` 생략 가능


line 184 ~ 230 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) addItemV4 추가
- line 200 `rejectValue(field, errorCode)`사용 `objectName`생략 `codes`도 코드 단축 가능
- line 204 ~ 207 `rejectValue(field, errorCode, arguments, defaultMessage)` 사용
- line 214 글로벌 오류는 `reject(errorCode, arguments, defaultMessage)` 사용

앞서 본 코드에서 `errorCode`에 V3 버전처럼 `required.item.itemName`하지 않고 `required`만 사용할 수 있는 이유
- `errorCode`의 오류 코드는 메시지에 등록된 코드 호출이 아님 `messageResolver`를 위한 코드 -> 뒤에서 설명

<br>

### 오류코드와 메시지 처리3 - *가장 중요*

위에서 설명한 `errorCode`의 처리 방식에 대해 설명
- 오류 메시지를 단순하게 만들면 범용성이 좋아지지만 메시지를 세밀하게 작성 불가.
- 가장 좋은 방법은 범용성으로 사용 -> 세밀한 메시지 필요한 경우 세밀한 메시지가 적용되도록
- ex: `required` 오류 코드 사용시 범용성 좋은 메시지보다 더 세밀한 메시지가 있으면 높은 우선순위로 사용
- 스프링은 이걸 `messageCodesResolver`로 이 기능을 지원함

<br>

### 오류코드와 메시지 처리4

[MessageCodesResolverTest](src/test/java/hello/itemservice/validation/MessageCodesResolverTest.java) `messageCodesResolver` 테스트 코드 생성
- line 13 `MessageCodesResolver` 인터페이스 생성
- line 16 ~ 19 `ObjectError` 테스트 코드
  - line 18 `messageCodes`를 출력해보면 `required.item`, `required` 두가지가 나옴
- line 22 ~ 30 `FieldError` 테스트 코드
  - 에러 코드를 기준으로 파라미터를 넣은 4가지의 조합으로 리스트를 만듬 위로 갈수록 디테일해짐
  - `required.item.itemName`: 에러코드 + 객체명 + 필드명
  - `required.itemName`: 에러코드 + 필드명
  - `required.java.lang.String`: 에러코드 + 타입
  - `required`: 에러코드

`MessageCodesResolver` 메시지 생성 규칙
- 객체 오류
  1. code + objectName
  2. code
- 필드 오류
  1. code + objectName + field
  2. code + field
  3. code + fieldType
  4. code

<br>

> 26.11.21

### 오류코드와 메시지 처리5

모든 오류 코드를 구체적으로 정의하면 관리가 힘듬

크게 중요하지 않은 메시지는 범용성, 중요한 메시지는 구체적으로 사용하는 방식이 효과적

line 6 ~ 35 [errors.properties](src/main/resources/errors.properties) 단계별 에러 메시지 추가

line 192 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) 공백처리와 같은 if문을 생략하고 싶으면 line 191 처럼 작성

<br>

### 오류코드와 메시지 처리6

검증 오류는 두가지 - 개발자가 직접 만든코드(rejectValue), 스프링이 직접 검증 오류에 추가(typeMismatch)

line 39 ~ 45 [errors.properties](src/main/resources/errors.properties) typeMismatch에 대한 메시지 추가

<br>

> 26.11.23

### Validator 분리1

[ItemValidator](src/main/java/hello/itemservice/web/validation/ItemValidator.java) 복잡한 검증 로직 분리 - 스프링이 제공하는 Validator 인터페이스 사용
- line 13 ~ 15 검증기가 Item클래스를 지원하는지 체크
- line 18 `Object`: 검증 대상 객체(Item)  `Errors`: `BindingResult`의 부모 클래스
- 기존 코드에서 `bindingResult`만 `errors`로 바꾸면 됨 

line 233 ~ 248 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) addItemV5 추가
- line 30 아까 만든 `Validator` 빈 등록
- line 235 `itemValidator.validate(target, bindingResult)` 검증 등록

<br>

### Validator 분리2

`validator` 인터페이스를 사용하는 이유 - 스프링의 추가적인 도움을 받을 수 있음

line 32 ~ 35 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) `@InitBinder`, `WebDataBinder` 사용
- `@InitBinder` 해당 컨트롤러에만 영향, 클로벌 설정을 별도로 해야함
- `WebDataBinder`에 검증기를 등록
- 해당 컨트롤러가 호출될 때 해당 메서드가 먼저 호출됨
- 이후 어떤 요청 매핑을 호출하더라도 해당 메서드 기능(검증) 사용 가능 - `@Validated`

line 251 ~ 264 [ValidationItemControllerV2](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV2.java) addItemV6 추가
- line 251 `@Validated` 어노테이션 추가 - 해당 타켓(Item)을 자동으로 검증 적용
- `@Validated`를 추가함으로써 V5처럼 클래스 호출 필요 없음

`WebDataBinder`에 여러 검증기가 등록되어 있을 때 구분할 수 있어야 함
- [ItemValidator](src/main/java/hello/itemservice/web/validation/ItemValidator.java)의 supports 메서드로 구분

검증기 글로벌 설정은 [ItemServiceApplication](src/main/java/hello/itemservice/ItemServiceApplication.java) 에 `WebMvcConfigurer` 인터페이스 상속받고 사용

<br>

>26.11.24

## 검증2 - Bean Validation

앞서 한 것처럼 검증을 매번 코드 작성은 상당히 번거로움.

이런 검증 로직을 모든 프로젝트에 적용할 수 있고 공통화, 표준화 -> `Bean Validation`

<br>

### Bean Validation - 시작

먼저 스프링과 통합하지 않고 순수 `Bean Validation`사용, 테스트 코드 추가

[Item](src/main/java/hello/itemservice/domain/item/Item.java) 객체에 어노테이션 추가
- itemName - `@NotBlank`
- price - `@NotNull`, `@Range(min = 1000, max = 1000000)`
- quantity - `@Max(value = 9999)`

[BeanValidationTest](src/test/java/hello/itemservice/validation/BeanValidationTest.java) `Bean Validation` 테스트 코드 참고

이후 스프링 적용을 위한 V3 버전 추가 [ValidationItemControllerV3](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV3.java), [HTML V3](src/main/resources/templates/validation/v3) 

<br>

### Bean Validation - 스프링 적용

[ValidationItemControllerV3](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV3.java) V2에 있던 `@InitVinder`, `ItemValidator`빈 등록을 제거 했는제도 정상 작동
- `validation` 라이브러리를 넣으면 자동으로 Bean Validator를 인지, 스프링에 통합함
- `LocalValidatorFactoryBean` 글로벌 Validator로 등록
- `Item`객체에 있는 `@NotNull` 어노테이션을 보고 검증을 수행
- 그냥 검증되는것이 아닌 컨트롤러에 있는 `@Validated`가 있어야 함

<br>

>26.11.25

### Bean Validation - 에러 코드

Bean Validation이 기본으로 제공하는 오류 메시지 변경

ex: `NotBlank` 오류 코드 기반  `MessageCodesResolver`를 통해 메시지 코드 생성
- NotBlank.item.itemName
- NotBlank.itemName
- NotBlank.java.lang.String
- NotBlank

이 메시지 코드를 [errors.properties](src/main/resources/errors.properties) 에 등록하면 오류 메시지 변경 가능 - line 42 ~ 45

<br>

### Bean Validation - 오브젝트 오류(ObjectError)

특정 필드(FieldError)가 아닌 오브젝트 오류(ObjectError)는 `@ScriptAssert()`를 사용하면 됨

[Item](src/main/java/hello/itemservice/domain/item/Item.java) 객체에 `@SsrciptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "...")` 추가

-> 제약 조건이 너무 많고 불편함. line 50 ~ 55 [ValidationItemControllerV3](src/main/java/hello/itemservice/web/validation/ValidationItemControllerV3.java) 직접 작성을 더 권장 

