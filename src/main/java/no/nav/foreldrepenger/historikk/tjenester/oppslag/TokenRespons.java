package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.util.StringUtil;

public class TokenRespons {

    private final String token;
    private final String tokenType;
    private final long expiresIn;

    @JsonCreator
    public TokenRespons(@JsonProperty("access_token") String access_token,
            @JsonProperty("token_type") String token_type, @JsonProperty("expires_in") long expires_in) {
        this.token = access_token;
        this.tokenType = token_type;
        this.expiresIn = expires_in;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[token=" + StringUtil.limit(token) + ", tokenType=" + tokenType
                + ", expiresIn=" + expiresIn + "]";
    }

}
