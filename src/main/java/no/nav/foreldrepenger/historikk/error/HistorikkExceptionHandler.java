package no.nav.foreldrepenger.historikk.error;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;

import javax.inject.Inject;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import no.nav.foreldrepenger.historikk.util.TokenUtil;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;

@ControllerAdvice
public class HistorikkExceptionHandler extends ResponseEntityExceptionHandler {
    @Inject
    private TokenUtil tokenUtil;
    private static final Logger LOG = LoggerFactory.getLogger(HistorikkExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException e, WebRequest request) {
        if (e.getStatusCode().equals(UNAUTHORIZED) || e.getStatusCode().equals(FORBIDDEN)) {
            return logAndHandle(e.getStatusCode(), e, request, tokenUtil.getExpiryDate());
        }
        return logAndHandle(e.getStatusCode(), e, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers, validationErrors(e));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @ExceptionHandler(UnexpectedInputException.class)
    public ResponseEntity<Object> handleUnexpectedException(UnexpectedInputException e, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @ExceptionHandler(JwtTokenUnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(JwtTokenUnauthorizedException e, WebRequest req) {
        return logAndHandle(UNAUTHORIZED, e, req);
    }

    @ExceptionHandler({ JwtTokenValidatorException.class })
    public ResponseEntity<Object> handleUnauthenticatedJwtException(JwtTokenValidatorException e, WebRequest req) {
        return logAndHandle(FORBIDDEN, e, req, e.getExpiryDate());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUncaught(Exception e, WebRequest req) {
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, Object... messages) {
        return logAndHandle(status, e, req, new HttpHeaders(), messages);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            Object... messages) {
        return logAndHandle(status, e, req, headers, asList(messages));
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            List<Object> messages) {
        var apiError = apiErrorFra(status, e, messages);
        LOG.warn("({}) {} {} ({})", maskertSubject(), status, apiError.getMessages(), status.value(), e);
        return handleExceptionInternal(e, apiError, headers, status, req);
    }

    private String maskertSubject() {
        return tokenUtil.authenticatedFødselsnummer()
                .map(Fødselsnummer::getMaskertFnr)
                .orElse("Uautentisert");
    }

    private static ApiError apiErrorFra(HttpStatus status, Exception e, List<Object> messages) {
        return new ApiError(status, e, messages);
    }

    private static List<String> validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(HistorikkExceptionHandler::errorMessage)
                .collect(toList());
    }

    private static String errorMessage(FieldError error) {
        return error.getField() + " " + error.getDefaultMessage();
    }
}
