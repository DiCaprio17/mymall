package com.hnz.mymall.product.exception;

import com.hnz.common.exception.BizCodeEnume;
import com.hnz.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangnuozhong
 * @create 2020-05-07-17:33
 * 统一处理异常
 */
@Slf4j
//RestControllerAdvice相当于@ResponseBody+@ControllerAdvice
@RestControllerAdvice(basePackages = "com.hnz.mymall.product.controller")
public class MymallExceptionControllerAdvice {

    //精确匹配异常
    //数据校验异常，感知异常需要用到注解,value = Exception.class设置所有异常都能处理
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            //获取错误的属性的名字,FieldError获取到错误提示
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        //使用自定义的统一的错误码
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    //走完精确匹配异常，走最大异常匹配(任意异常)
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("错误：", throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
