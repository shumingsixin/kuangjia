package com.example.demo.model;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Aspect//标记这是个切面
@Component
@Slf4j
public class AopLog {
    private static final String UNKOWN = "unkonwn";

    //切入点 切入该路径 访问修饰符 返回类型 类路径 方法名 (..)表示多个任意参数
    @Pointcut("execution(public * com.example.demo.controller.*Controller.*(..))")
    public void log() {

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Log {
        //线程id
        private String threadId;
        private String threadName;
        private String ip;
        private String url;
        //请求方式GET PUT
        private String httpMethod;
        //请求的类方法
        private String classMethod;
        //请求参数
        private Object requestParams;
        //返回参数
        private Object result;
        //接口耗时
        private Long timeCost;
        //操作系统
        private String os;
        //浏览器
        private String browser;
        //用户权限
        private String userAgent;
    }

    /**
     * 获取方法参数名和参数值
     *
     * @param point
     * @return
     */
    private Map<String, Object> getNameAndValue(ProceedingJoinPoint point) {
        //获取各种接口签名
        final Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        final String[] names = methodSignature.getParameterNames();
        final Object[] args = point.getArgs();
        if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
            return Collections.emptyMap();
        }
        if (names.length != args.length) {
            log.warn("{}方法参数名和参数值数量不一致", methodSignature.getName());
            return Collections.emptyMap();
        }
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], args[i]);
        }
        return map;
    }

    /**
     * 获取ip地址
     *
     * @param request http应用请求
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded_for");
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            //获取本机真正的地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return
     * @throws Throwable 异常信息
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        //获取程序请求参数参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //映射非空参数
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        //打印请求相关参数
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        final Log l = Log.builder()
                .threadId(Long.toString(Thread.currentThread().getId()))
                .threadName(Thread.currentThread().getName())
                .ip(getIp(request))
                .url(request.getRequestURL().toString())
                .classMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(), point.getSignature().getName()))
                .httpMethod(request.getMethod())
                .requestParams(getNameAndValue(point))
                .result(result)
                .timeCost(System.currentTimeMillis() - startTime)
                .userAgent(header)
                .browser(userAgent.getBrowser().toString().toLowerCase(Locale.ROOT))
                .os(userAgent.getOperatingSystem().toString())
                .build();
        log.info("Request Log Info: {}", JSONUtil.toJsonStr(l));
        return result;
    }


}
