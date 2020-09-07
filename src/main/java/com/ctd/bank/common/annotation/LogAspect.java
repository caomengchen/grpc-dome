package com.ctd.bank.common.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ctd.bank.common.bean.model.BusinessLogModel;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.common.dao.IBaseDAO;
import com.ctd.bank.common.util.BaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Component
@Slf4j
public class LogAspect {
	
	private static ThreadLocal<String> LOCAL = new ThreadLocal<>();
	
	public static String getLocalValue(){
		return LOCAL.get();
	}
	
	@SuppressWarnings("rawtypes")
	@Resource
	private IBaseDAO baseDAO;
	
	
	@Pointcut("@annotation(com.ctd.bank.common.annotation.requestBodyLog)")
	private void requestBodyAspect(){}
	
	@SuppressWarnings("unchecked")
	@Around("requestBodyAspect()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint){  
		BusinessLogModel businessLogModel = new BusinessLogModel();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        
        Object result = null;
        Result retRes = null;
        HttpServletRequest httpRequest = null;
        Map<String, Object> paramMap = new HashMap<>();
        try{
	        for (int i = 0; i < args.size(); i++) {
				if(args.get(i) instanceof HttpServletRequest){
					httpRequest = (HttpServletRequest)args.get(i);
					paramMap.put("parameterMap",httpRequest.getParameterMap());

					businessLogModel.setOperIp(BaseUtils.encodeUTF8(getIp()));
				}else{
					if(!(args.get(i) instanceof HttpServletResponse)){
						paramMap.put(args.get(i).getClass().getName(), args.get(i));
					}
					
				}
			}
        }catch(Exception e){
    	    System.out.println("日志获取数据 失败！");
    	    e.printStackTrace();
        }
        businessLogModel.setRequestArgs(JSON.toJSONString(paramMap, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.WriteNullListAsEmpty));
        try {
        	//放入
        	String num = getNum();
        	LOCAL.set(num);
        	businessLogModel.setBusinessNo(num);
        	
            result = joinPoint.proceed();
            
            if(result instanceof Result){
            	 retRes = (Result) result;
            }
            if(retRes != null){
            	businessLogModel.setReturnInfo(JSON.toJSONString(retRes));
            	businessLogModel.setStatus(retRes.getCode().toString());//1 0 成功   -1 失败 
            	if(retRes.getCode() != 0){
            		businessLogModel.setBz(retRes.getMsg());
            	}
            }
            String businessName = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(requestBodyLog.class).businessName();
            businessLogModel.setDescrip(businessName);
            System.out.println("记录日志: " + businessLogModel.toString());
        } catch (Throwable e) {
        	log.info("异常信息:{" + e.getMessage() + "}");
        }finally{
        	if(LOCAL != null){
        		//移除
        		LOCAL.remove();
        	}
        }
        try {
			baseDAO.insert("tb.log.businessInsert", businessLogModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
    }
	
	private String getIp(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	
	private String getNum(){
		String num = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + (new Random().nextInt(900) + 100);
		System.out.println("流水号：" + num);
		return num;
	}
}
