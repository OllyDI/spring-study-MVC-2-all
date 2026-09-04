package hello.exception.exHandler.advice;

import hello.exception.exHandler.ErrorResult;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice
 *
 * 대상을 지정해주지 않으면 모든 컨트롤러에 적용이 됨
 *
 * 대상 컨트롤러 지정 방법 3가지
 * Target all Controllers annotated with @RestController -> 어노테이션 지정방법
 * @ControllerAdvice(annotations = RestController.class)
 * public class ExampleAdvice1 {}
 *
 * Target all Controllers within specific packages -> 패키지 지정방법
 * @ControllerAdvice("org.example.controllers")
 * public class ExampleAdvice2 {}
 *
 * Target all Controllers assignable to specific classes -> 클래스 지정방법, 부모 클래스 지정 시 자식 클래스도 적용됨
 * @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
 * public class ExampleAdvice3 {}
 */


@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExhandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }


    // 위의 두 예외처리에서 처리하지 못한 예외처리는 모두 여기서 처리함
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
