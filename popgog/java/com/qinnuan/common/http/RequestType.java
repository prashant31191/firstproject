package com.qinnuan.common.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by showu on 13-7-4.
 */
@Retention(RUNTIME)
@Target( { ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD })
public @interface RequestType {
    HttpMethod type() default HttpMethod.GET;
}
