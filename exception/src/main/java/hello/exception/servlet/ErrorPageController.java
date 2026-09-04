package hello.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";


    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest req, HttpServletResponse res) {
        log.info("errorPage 404");
        printErrorInfo(req);
        return "/error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest req, HttpServletResponse res) {
        log.info("errorPage 500");
        printErrorInfo(req);
        return "/error-page/500";
    }


    // Accept가 application/json인 경우에만 호출 -> 서블릿을 이용한 api 에러 처리
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest req, HttpServletResponse res) {
        log.info("API errorPage 500");

        HashMap<String, Object> result = new HashMap<>();
        Exception ex = (Exception) req.getAttribute(ERROR_EXCEPTION);
        result.put("status", req.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }


    private void printErrorInfo(HttpServletRequest req) {
        log.info("ERROR_EXCEPTION_TYPE: {}", req.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_EXCEPTION: {}", req.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_MESSAGE: {}", req.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", req.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", req.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", req.getAttribute(ERROR_STATUS_CODE));

        log.info("dispatchType: {}", req.getDispatcherType());
    }
}
