package com.example.PartTrip.global.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class StaticResponseCacheAdvice implements ResponseBodyAdvice<Object> {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    static final Map<String, Duration> MAX_AGE = new LinkedHashMap<>();

    static {
        MAX_AGE.put("/api/planners/blocks", Duration.ofHours(1));
        MAX_AGE.put("/api/main/countries", Duration.ofHours(1));
        MAX_AGE.put("/api/main/cities", Duration.ofHours(1));
        MAX_AGE.put("/api/main/festivals", Duration.ofHours(1));
        MAX_AGE.put("/api/main/tour-place", Duration.ofMinutes(10));
        MAX_AGE.put("/api/main/tour-place/more", Duration.ofMinutes(10));
        MAX_AGE.put("/api/main/popular-cities", Duration.ofMinutes(5));
    }

    static Duration maxAgeFor(String method, String path) {
        if (!"GET".equals(method)) {
            return null;
        }
        for (Map.Entry<String, Duration> entry : MAX_AGE.entrySet()) {
            if (MATCHER.match(entry.getKey(), path)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass().isAnnotationPresent(RestController.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        Duration maxAge = maxAgeFor(request.getMethod().name(), request.getURI().getPath());
        if (maxAge != null) {
            response.getHeaders().setCacheControl(CacheControl.maxAge(maxAge).cachePrivate());
        }
        return body;
    }
}
