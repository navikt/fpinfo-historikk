package no.nav.foreldrepenger.historikk.error;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.callId;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;
import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private final HttpStatus status;
    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
    private final List<String> messages;
    private final String uuid;

    public ApiError(HttpStatus status, Throwable t) {
        this(status, t, emptyList());
    }

    public ApiError(HttpStatus status, Object... objects) {
        this(status, null, Arrays.asList(objects));
    }

    ApiError(HttpStatus status, Throwable t, List<Object> objects) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.messages = messages(t, objects);
        this.uuid = callId();
    }

    public String getUuid() {
        return uuid;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    private static List<String> messages(Throwable t, List<Object> objects) {
        if (objects == null) {
            return emptyList();
        }
        List<Object> messages = List.of(objects);
        if (t != null) {
            messages.add(getMostSpecificCause(t).getMessage());
        }
        return safeStream(messages)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[status=" + status + ", timestamp=" + timestamp + ", messages=" + messages
                + ", uuid=" + uuid + "]";
    }
}