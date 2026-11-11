# 스프링-MVC-2-복습

>26.11.11

## 타임리프 - 스프링 통합과 폼

과거 `thymeleaf-basic` 프로젝트에서 만든 타임리프를 스프링과 통합, 개선 프로젝트

### 입력 폼 처리

line 62 ~ 66 [FormItemController](src/main/java/hello/itemservice/web/form/FormItemController.java) Item 객체를 모델에 넘겨줌
- line 21 [addForm.html](src/main/resources/templates/form/addForm.html) `th:object="${item}"` 폼에서 사용할 객체 지정
- line 26 ~ `th:field` 를 사용하여 태그의 `id`, `name`, `value`를 자동 생성
- `th:field="*{}"` 선택 변수식을 사용하면 `th:object="${item}"`에 소속된 변수로 판단 -> `item.` 생략 가능

[editForm.html](src/main/resources/templates/form/editForm.html) 도 `th:object`를 사용하여 쉽게 수정 및 사용

***참고 input태그에 id도 지워도 상관X id가 label에 묶여있어 밑줄이 생겨서 놔둠**

<br>

### 요구사항 추가

타임리프를 이용 폼에서 체크박스, 라디오버튼, 셀렉트박스를 편리하게 사용하는 방법을 알기위해 요구사항 추가

- 판매 여부 
  - 판매 오픈 여부
  - 체크 박스로 선택할 수 있다.

- 등록 지역
  - 서울, 부산, 제주
  - 체크 박스로 다중 선택할 수 있다.

- 상품 종류
  - 도서, 식품, 기타
  - 라디오 버튼으로 하나만 선택할 수 있다. 

- 배송 방식
  - 빠른 배송
  - 일반 배송
  - 느린 배송
  - 셀렉트 박스로 하나만 선택할 수 있다.

[ItemType](src/main/java/hello/itemservice/domain/item/ItemType.java) 상품 종류, [DeliveryCode](src/main/java/hello/itemservice/domain/item/DeliveryCode.java) 배송 방식, line 15 ~ 18 [Item](src/main/java/hello/itemservice/domain/item/Item.java) 아이템 추가

<br>

### 체크박스 - 단일1

line 40 ~ 45 [addForm.html](src/main/resources/templates/form/addForm.html) 단일 체크박스 추가
- 순수 html로 체크박스 구현시 문제 -> 체크 안하고 form을 보내면 체크박스 데이터가 아에 전송 안됨
- 상품 수정시 문제 발생 -> 기존에 체크가 되어있는데 null이 넘어와 체크 해제로 저장될 가능성
- line 45 히든 필드 `name="_open"` 사용하면 해결 가능 -> 스프링 기능

<br>

### 체크박스 - 단일2

앞서 한 체크박스를 타임리프로 수정

line 46 ~ 47 [addForm.html](src/main/resources/templates/form/addForm.html) 타임리프 사용
- `th:field`사용 - 위의 단일1 처럼 히든필드를 만들지 않아도 알아서 생성

line 43 ~ 49 [item.html](src/main/resources/templates/form/item.html) 상품 상세 페이지 체크박스 추가
- `th:field`가 체크박스 true, false에 따라서 `checked` 알아서 넣어줌

line 41 ~ 47 [editForm.html](src/main/resources/templates/form/editForm.html) 상품 수정 페이지 체크박스 추가
line 30 ~ 38 [ItemRepository](src/main/java/hello/itemservice/domain/item/ItemRepository.java) 업데이트 수정