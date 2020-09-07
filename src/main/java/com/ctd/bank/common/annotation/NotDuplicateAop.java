package com.ctd.bank.common.annotation;

/**
 * Created by Hwong on 2019/4/25
 */

import com.ctd.bank.common.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Aspect
@Component
public class NotDuplicateAop {

    private static volatile  Set<String> KEY =  new ConcurrentSkipListSet<>();
    private static volatile  Map<String,Long> VALUE =  new ConcurrentSkipListMap<>();
    private static Lock lock = new ReentrantLock();// 锁对象
    @Pointcut("@annotation(com.ctd.bank.common.annotation.NotDuplicate)")
    public void duplicate() {
    }

    /**
     * 对方法拦截后进行参数验证
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("duplicate()")
    public  Object duplicate(ProceedingJoinPoint pjp) throws Throwable {
        lock.lock();
        MethodSignature msig = (MethodSignature) pjp.getSignature();
        Method currentMethod = pjp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //拼接签名
        StringBuilder sb = new StringBuilder(currentMethod.toString());
        Object[] args = pjp.getArgs();
        for (Object object : args) {
            if(object != null){
                sb.append(object.getClass().toString());
                sb.append(object.toString());
            }
        }
        String sign = sb.toString();
//        List<Object> arg = Arrays.asList(pjp.getArgs());
//        HttpServletRequest httpRequest = null;
//        for (int i = 0; i < arg.size(); i++) {
//            if(arg.get(i) instanceof HttpServletRequest){
//                httpRequest = (HttpServletRequest)arg.get(i);
//                break;
//            }
//        }
//        Map<String, String[]> parameterMap = new HashMap<>();
//        if(httpRequest != null){
//            parameterMap = httpRequest.getParameterMap();
//        }
        boolean success = KEY.add(sign);
        try {
            if(!success){
                System.out.println("拦截参数："+sign);
                throw new BusinessException("重复调用");
            }else {
                VALUE.put(sign,new Date().getTime());
                return pjp.proceed();
            }
        } finally {
            lock.unlock();
            for (String key : VALUE.keySet()) {
                if(new Date().getTime() - VALUE.get(key) >300000){    //删除5分钟前的访问
                    VALUE.remove(key);
                    KEY.remove(key);
                }
            }
        }
    }
}