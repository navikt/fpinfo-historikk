package no.nav.foreldrepenger.historikk.http.filter;

import static no.nav.foreldrepenger.historikk.http.filter.FilterRegistrationUtil.always;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class HeadersToMDCFilterRegistrationBean extends FilterRegistrationBean<HeadersToMDCFilterBean> {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersToMDCFilterRegistrationBean.class);

    public HeadersToMDCFilterRegistrationBean(HeadersToMDCFilterBean headersFilter) {
        setFilter(headersFilter);
        setOrder(1);
        setUrlPatterns(always());
        LOG.info("Registrert filter {}", this.getClass().getSimpleName());
    }
}
