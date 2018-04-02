package com.mo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(value = {ElementType.TYPE})
@interface EnableMoRocketMq {

}
