# Spring MVC 기본기능 Study Project
## Project 개요
- 프로젝트 선택
    - Project: Gradle Project
    - Spring Boot: 2.7.3
    - Language: Java
    - Packaging: Jar
    - Java: 11
- Project Metadata
    - Group: com.example
    - Artifact: springmvc
    - Name: SpringMVC
    - Package name: com.example.springmvc
- Dependencies: **Spring Web**, **Thymeleaf**, **Lombok**

## Package Design
```
└── src
    ├── main
    │   ├── java
    │   │   └── com.example.springmvc
    │   │               ├── basic
    │   │               │      ├── requestmapping
    │   │               │      │       ├── MappingController(C)            |  요청 맵핑 예제 컨트롤러
    │   │               │      │       ├── MappingClassController(C)       |  요청 맵핑 API 예제 컨트롤러
    │   │               │      │       ├── RequestHeaderController(C)      |  HTTP 요청 기본, 헤더 조회 컨트롤러
    │   │               │      │       ├── RequestParamController(C)       |  HTTP 요청 파라미터(쿼리 파라미터, HTML Form) 조회 컨트롤러
    │   │               │      │       ├── RequestBodyStringController(C)  |  HTTP 요청 메세지 body - Text 조회 컨트롤러
    │   │               │      │       ├── RequestBodyJsonController(C)    |  HTTP 요청 메세지 body - JSON 조회 컨트롤러
    │   │               │      │       ├── ResponseViewController(C)       |  HTTP 응답 View 컨트롤러
    │   │               │      │       └── ResponseBodyController(C)       |  HTTP 응답 메세지 body 컨트롤러
    │   │               │      ├── HelloData(C)           |  request, response data용 object
    │   │               │      └── LogTestController(C)   |  Log Test용 컨트롤러
    │   ├── resource
    │   │   ├── static
    │   │   │     └── index.html    |   Welcome page
    │   │   ├── template
    │   │   │     └── response
    │   │   │            └── hello.html  |  thymeleaf Test page
    │   │   └── application.yaml
```

## 로깅
- SLF4J
- private Logger log = LoggerFactory.getLogger(getClass());
- private static final Logger log = LoggerFactory.getLogger(Xxx.class)
- @Slf4j : 롬복 사용 가능

## HTTP 요청 
- 요청 - 기본, 헤더 조회
- 요청 파라미터 - 쿼리 파라미터, HTML Form
- 요청 파라미터 - @RequestParam
- 요청 파라미터 - @ModelAttribute
- 요청 메시지 - 단순 텍스트
- 요청 메시지 - JSON

## HTTP 응답
- 응답 - 정적 리소스, 뷰 템플릿
- API, 메시지 바디에 직접 입력

## HTTP 메시지 컨버터
- 뷰 템플릿으로 HTML을 생성해서 응답하는 것이 아니라, HTTP API처럼 JSON 데이터를 HTTP 메시지 바디에서 직접 읽거나 쓰는 경우 HTTP 메시지 컨버터를 사용하면 편리하다.
- HTTP 메시지 컨버터 인터페이스 : org.springframework.http.converter.HttpMessageConverter
- 스프링 부트는 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 둘을 체크해서 사용여부를 결정한다. 
```
0 = ByteArrayHttpMessageConverter
1 = StringHttpMessageConverter
2 = MappingJackson2HttpMessageConverter
.....
```

## 요청 매핑 핸들러 어뎁터 구조
- HTTP 메시지 컨버터는 스프링 MVC 어디쯤에서 사용?
- 애노테이션 기반의 컨트롤러, 그러니까 @RequestMapping 을 처리하는 핸들러 어댑터인 **RequestMappingHandlerAdapter(요청 매핑 헨들러 어뎁터)에 있다.**
- **요청의 경우** @RequestBody를 처리하는 ArgumentResolver가 있고, HttpEntity를 처리하는 ArgumentResolver가 있다.
  - **이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성하는 것이다.**
- **응답의 경우** @ResponseBody 와 HttpEntity 를 처리하는 ReturnValueHandler 가 있다.
  - **그리고 여기에서 HTTP 메시지 컨버터를 호출해서 응답 결과를 만든다.**
- 스프링 MVC는
  - @RequestBody, @ResponseBody가 있으면 RequestResponseBodyMethodProcessor(ArgumentResolver)를 사용한다.
  - HttpEntity가 있으면 HttpEntityMethodProcessor(ArgumentResolver)를 사용한다.