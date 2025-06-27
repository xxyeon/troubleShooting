package com.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //필드에 붙일 어노테이션이다.
@Retention(RetentionPolicy.RUNTIME) //런타임에 동작한다.
public @interface ExcelColumn {
	String headerName() default "";

}
