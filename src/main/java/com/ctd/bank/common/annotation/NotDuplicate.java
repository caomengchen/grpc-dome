package com.ctd.bank.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by Hwong on 2019/4/25
 */

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotDuplicate {
}