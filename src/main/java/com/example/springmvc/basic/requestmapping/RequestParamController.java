package com.example.springmvc.basic.requestmapping;

import com.example.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩
     * @RequestParam("username") String memberName == request.getParameter("username")
     * @ResponseBody 추가 == @RestController
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     **/
    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     **/
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * String, int, Integer등의 단순타입이면 @RequestParam도 생략 가능
     * @RequestParam 애노테이션을 생략하면 스프링 MVC는 내부에서 required=false를 적용
     **/
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터 필수 여부 - requestParamRequired
     *
     * @RequestParam required=true(default)
     * /request-param-required -> username이 없으므로 예외
     * <p>
     * 주의!
     * /request-param-required?username= -> 빈문자로 통과
     * <p>
     * 주의!
     * /request-param-required
     * <p>
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는 defaultValue 사용)
     **/
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username, @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 기본 값 적용 - requestParamDefault
     *
     * @RequestParam - defaultValue 사용
     * <p>
     * 참고: defaultValue는 빈 문자의 경우에도 적용
     * * /request-param-default?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username,
                                      @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터를 Map으로 조회하기 - requestParamMap
     *
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     * 파라미터의 값이 1개가 확실하다면 Map을 사용해도 되지만, 그렇지 않다면 MultiValueMap 사용
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("paramMap={}", paramMap);
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }


    /**
     * @ModelAttribute 사용
     *
     * 스프링 MVC는 @ModelAttribute가 있으면 다음을 실행한다.
     * 1. HelloData 객체를 생성한다.
     * 2. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다.
     * 3. 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.
     *
     * @ModelAttribute : 생략도 가능
     * @RequestParam 생략할 수 있으니 혼란이 발생할 수 있다.
     *
     * 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
     * String , int , Integer 같은 단순 타입 = @RequestParam
     * 나머지 = @ModelAttribute (argument resolver로 지정해둔 타입 외 == 본인이 정의한 class type)
     *
     **/
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("HelloData={}", helloData);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    /**
     * @ModelAttribute : 생략도 가능
     **/
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("HelloData={}", helloData);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
}
