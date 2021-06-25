package com.example.demo.hander;

import com.example.demo.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ProjectExceptionHander {
    private static final String DEFAULT_ERROE_VIEW = "page/error";

    /**
     * 统一处理json问题
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public ApiResponse JsonErrorHander(JsonException exception) {
        log.error("[JsonException] : {}", exception.getMessage());
        return ApiResponse.ofException(exception);
    }

    /**
     * 运行时错误json处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ApiResponse runErrorHander(RuntimeException e) {
        log.error("[JsonException] : {}", e.getMessage());
        return ApiResponse.ofException(e);
    }

    /**
     * 统一处理页面问题
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = PageException.class)
    public ModelAndView pageErrorHander(PageException exception) {
        log.error("[PageException] : {}", exception.getMessage());
        ModelAndView view = new ModelAndView();
        view.addObject("message", exception.getMessage());
        view.setViewName(DEFAULT_ERROE_VIEW);
        return view;
    }


}
