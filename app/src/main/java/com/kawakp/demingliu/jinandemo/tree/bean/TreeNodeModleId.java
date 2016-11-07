package com.kawakp.demingliu.jinandemo.tree.bean;

/**
 * Created by deming.liu on 2016/10/28.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface TreeNodeModleId
{
}
