package no.nav.foreldrepenger.historikk.http.filter;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CONSUMER_ID;
import static no.nav.foreldrepenger.common.util.MDCUtil.toMDC;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.historikk.http.CallIdGenerator;

@Component
public class HeadersToMDCFilterBean extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersToMDCFilterBean.class);

    private final String applicationName;
    private final CallIdGenerator generator;

    @Autowired
    public HeadersToMDCFilterBean(@Value("${spring.application.name:fpinfo-historikk}") String applicationName,
                                  CallIdGenerator generator) {
        this.applicationName = applicationName;
        this.generator = generator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        putValues(HttpServletRequest.class.cast(request));
        chain.doFilter(request, response);
    }

    private void putValues(HttpServletRequest req) {
        try {
            toMDC(NAV_CONSUMER_ID, req.getHeader(NAV_CONSUMER_ID), applicationName);
            toMDC(NAV_CALL_ID, req.getHeader(NAV_CALL_ID), generator.create());
        } catch (Exception e) {
            LOG.warn("Noe gikk galt ved setting av MDC-verdier for request. MDC-verdier er inkomplette!", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [applicationName=" + applicationName + "]";
    }

}
