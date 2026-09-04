package hello.exception.api;


import hello.exception.exHandler.ErrorResult;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ExceptionHandler
 *
 * 일반적으로 가장 많이 쓰는 예외처리 -> API 처리시에만 사용
 * 해당 컨트롤러안에서만 처리됨 -> RestControllerAdvice를 사용하면 가능
 * 지정한 예외 + 자식 클래스는 모두 잡을 수 있음
 * 어노테이션의 예외 생략 가능 -> 메서드의 파라미터의 예외로 지정됨
 * 에러 코드는 @ResponseStatus를 이용하거나 ResponseEntity를 통해 반환
 */

@Slf4j
@RestController
public class ApiExceptionV2Controller {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExhandler(IllegalArgumentException e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//
//    // 위의 두 예외처리에서 처리하지 못한 예외처리는 모두 여기서 처리함
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) throws UserException {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
