package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResponseStatusExceptionResolver
 * 예외에 따라서 HTTP 상태 코드 지정 두 가지 경우 처리
 * @ResponseStatus
 * ResponseStatusException
 * reason을 MessageSource 에서 찾는 기능 제공 -> messages.properties
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
