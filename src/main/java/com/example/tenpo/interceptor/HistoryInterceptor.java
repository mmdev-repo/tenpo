package com.example.tenpo.interceptor;

import com.example.tenpo.model.HistoryAverage;
import com.example.tenpo.repository.TenpoRepositoryImpl;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class HistoryInterceptor implements HandlerInterceptor {

    @Autowired
    private TenpoRepositoryImpl tenpoRepositoryImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String input = new Gson().toJson(parameterMap);

            HistoryAverage history = HistoryAverage.builder()
                    .date(LocalDateTime.now())
                    .endpoint(request.getRequestURI())
                    .input(input)
                    .build();

            request.setAttribute("history", history);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HistoryAverage history = (HistoryAverage) request.getAttribute("history");
        if (history != null) {
            if (history.getEndpoint().startsWith("/error") && ex == null) {
                return;
            }
            if (ex != null) {
                history.setOutput("Error: " + ex.getMessage());
            } else {
                ResponseWrapper responseWrapper = new ResponseWrapper(response);
                String responseBody = responseWrapper.getCapturedResponse();
                history.setOutput(responseBody);
            }

            tenpoRepositoryImpl.save(history);
        }
    }
}
