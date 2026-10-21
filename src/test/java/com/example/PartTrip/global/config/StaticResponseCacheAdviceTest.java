package com.example.PartTrip.global.config;

import com.example.PartTrip.global.exception.GlobalExceptionHandler;
import com.example.PartTrip.main.controller.FestivalController;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.main.service.FestivalService;
import com.example.PartTrip.planner.controller.PlannerDraftController;
import com.example.PartTrip.planner.service.PlannerDraftService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StaticResponseCacheAdviceTest {

    private final StaticResponseCacheAdvice advice = new StaticResponseCacheAdvice();

    @Test
    void 목록에_있는_GET_만_캐시_시간이_정해진다() {
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/planners/blocks")).isEqualTo(Duration.ofHours(1));
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/main/tour-place/more")).isEqualTo(Duration.ofMinutes(10));
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/main/popular-cities")).isEqualTo(Duration.ofMinutes(5));
    }

    @Test
    void 무장애_정보와_사용자별_응답과_GET_이_아닌_요청은_제외한다() {
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/main/tour-place/7/accessibility")).isNull();
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/planners")).isNull();
        assertThat(StaticResponseCacheAdvice.maxAgeFor("GET", "/api/notifications")).isNull();
        assertThat(StaticResponseCacheAdvice.maxAgeFor("POST", "/api/planners/blocks")).isNull();
    }

    @Test
    void 응답_헤더에_private_max_age_가_붙는다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/main/festivals");
        MockHttpServletResponse servlet = new MockHttpServletResponse();

        ServletServerHttpResponse response = new ServletServerHttpResponse(servlet);
        advice.beforeBodyWrite("body", null, null, null, new ServletServerHttpRequest(request), response);
        response.flush();

        assertThat(servlet.getHeader("Cache-Control")).contains("max-age=3600").contains("private");
    }

    @Test
    void 다른_경로에는_헤더를_붙이지_않는다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/planners");
        MockHttpServletResponse servlet = new MockHttpServletResponse();

        ServletServerHttpResponse response = new ServletServerHttpResponse(servlet);
        advice.beforeBodyWrite("body", null, null, null, new ServletServerHttpRequest(request), response);
        response.flush();

        assertThat(servlet.getHeader("Cache-Control")).isNull();
    }

    @Test
    void 정상_컨트롤러_응답에만_적용하고_오류_핸들러_응답은_건너뛴다() throws Exception {
        MethodParameter controller = new MethodParameter(FestivalController.class.getMethods()[0], -1);
        MethodParameter errorHandler = new MethodParameter(
                GlobalExceptionHandler.class.getDeclaredMethods()[0], -1);

        assertThat(advice.supports(controller, null)).isTrue();
        assertThat(advice.supports(errorHandler, null)).isFalse();
    }

    @Test
    void MVC_를_거쳐도_정상_응답에만_붙는다() throws Exception {
        MockMvc blocks = MockMvcBuilders
                .standaloneSetup(new PlannerDraftController(mock(PlannerDraftService.class)))
                .setControllerAdvice(advice, new GlobalExceptionHandler())
                .build();
        blocks.perform(get("/api/planners/blocks"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", org.hamcrest.Matchers.containsString("max-age=3600")));

        FestivalService festivals = mock(FestivalService.class);
        given(festivals.getFestivals("일본", null, null)).willThrow(new NotFoundException("없음"));
        MockMvc failing = MockMvcBuilders
                .standaloneSetup(new FestivalController(festivals))
                .setControllerAdvice(advice, new GlobalExceptionHandler())
                .build();
        failing.perform(get("/api/main/festivals").param("countryName", "일본"))
                .andExpect(status().isNotFound())
                .andExpect(header().doesNotExist("Cache-Control"));
    }
}
