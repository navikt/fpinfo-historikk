package no.nav.foreldrepenger.historikk.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.util.MDCUtil.callId;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private final HttpStatusCode status;
    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
    private final List<String> messages;
    private final String uuid;

    public ApiError(HttpStatusCode status, Throwable t) {
        this(status, t, emptyList());
    }

    public ApiError(HttpStatusCode status, Object... objects) {
        this(status, null, Arrays.asList(objects));
    }

    ApiError(HttpStatusCode status, Throwable t, List<Object> objects) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.messages = messages(t, objects);
        this.uuid = callId();
    }

    public String getUuid() {
        return uuid;
    }

    public HttpStatusCode getStatus() {
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
        var messages = new ArrayList<>(objects);
        if (t != null) {
            messages.add(getMostSpecificCause(t).getMessage());
        }
        return safeStream(messages)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[status=" + status + ", timestamp=" + timestamp + ", messages=" + messages
                + ", uuid=" + uuid + "]";
    }
}
