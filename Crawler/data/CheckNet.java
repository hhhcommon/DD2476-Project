1
https://raw.githubusercontent.com/niufuwei/block_chian/master/Stock/app/src/main/java/com/hjq/demo/aop/CheckNet.java
package com.hjq.demo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/01/11
 *    desc   : 检测网络注解
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CheckNet {}