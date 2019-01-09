package ru.kolaer.server.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import ru.kolaer.common.dto.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.core.config.PathMapping;
import ru.kolaer.server.core.service.UrlSecurityService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Фильтер позволяющи динамически добовлять ограничения на URL.
 */
@Slf4j
public class SecurityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {

    private UrlSecurityService urlSecurityService;

    public SecurityMetadataSourceFilter(UrlSecurityService urlSecurityService) {
        this.urlSecurityService = urlSecurityService;
    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url = PathMapping.DISPATCHER_SERVLET + fi.getRequestUrl();
        String method = fi.getHttpRequest().getMethod();

        return Optional.ofNullable(getUrlSecurity(url, method))
                .map(this::getRoles)
                .orElse(Collections.emptyList());
    }

    private UrlSecurityDto getUrlSecurity(String url, String method) {
        String[] urlOriginPatterns = url.split("/");

        List<UrlSecurityDto> urlPaths = urlSecurityService.getPathByMethod(method);

        for (UrlSecurityDto urlPath : urlPaths) {
            if(checkUrlPattern(urlOriginPatterns, urlPath.getUrl())) {
                return urlPath;
            }
        }

        return null;
    }

    private boolean checkUrlPattern(String[] urlOriginPatterns, String urlPattern) {
        String[] urlPatterns = urlPattern.split("/");

        if(urlPatterns.length == urlOriginPatterns.length) {
            for (int indexUrl = 0; indexUrl < urlOriginPatterns.length; indexUrl++) {
                String urlPatternUrl = urlPatterns[indexUrl];
                String urlOriginUrl = urlOriginPatterns[indexUrl];

                if(urlPatternUrl.contains("{")) {
                    urlPatternUrl = urlOriginUrl;
                }

                if(!urlPatternUrl.equals(urlOriginUrl)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private Collection<ConfigAttribute> getRoles(UrlSecurityDto urlPath) {
        List<String> accesses = urlSecurityService.getAccesses(urlPath);

        return accesses.contains("ALL")
                ? Collections.emptyList()
                : accesses.stream()
                .map(SecurityConfig::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}