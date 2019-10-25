package no.nav.foreldrepenger.historikk.util;

import static org.springframework.core.env.Profiles.of;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.core.env.Environment;

public final class EnvUtil {
    public static final String DEFAULT = "default";
    public static final String DEV = "dev";
    public static final String LOCAL = "local";
    public static final String INCLUSTER = "!" + LOCAL;
    public static final Marker CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL");

    public static final String DEV_GCP = "dev-gcp";
    public static final String PROD_GCP = "prod-gcp";

    public static final String PROD_FSS = "prod-fss";
    public static final String DEV_FSS = "dev-fss";

    private EnvUtil() {
    }

    public static boolean isDevOrLocal(Environment env) {
        return isDev(env) || isLocal(env);
    }

    public static boolean isLocal(Environment env) {
        return env.acceptsProfiles(of(LOCAL));
    }

    public static boolean isDev(Environment env) {
        return env.acceptsProfiles(of(DEV));
    }

    public static boolean isProd(Environment env) {
        return !isDevOrLocal(env);
    }
}
