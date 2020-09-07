package com.ctd.bank.common.annotation;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessLog {
	String description() default "";
	String businessName() default "";
	Class[] paramClass();
}
