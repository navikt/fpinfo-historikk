package no.nav.foreldrepenger.historikk.http;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CONSUMER_ID;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.toMDC;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@Order(LOWEST_PRECEDENCE)
public class HeadersToMDCFilterBean extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersToMDCFilterBean.class);

    private final CallIdGenerator generator;
    private final String applicationName;

    @Inject
    public HeadersToMDCFilterBean(CallIdGenerator generator,
            @Value("${spring.application.name:fpinfo-historikk}") String applicationName) {
        this.generator = generator;
        this.applicationName = applicationName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        putValues(HttpServletRequest.class.cast(request));
        chain.doFilter(request, response);
    }

    private void putValues(HttpServletRequest req) {
        try {
            toMDC(NAV_CONSUMER_ID, req.getHeader(NAV_CONSUMER_ID), applicationName);
            toMDC(NAV_CALL_ID, req.getHeader(NAV_CALL_ID), generator.create());
        } catch (Exception e) {
            LOG.warn("Feil ved setting av MDC-verdier for {}, MDC-verdier er inkomplette", req.getRequestURI(), e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [generator=" + generator + ", applicationName=" + applicationName + "]";
    }

}
