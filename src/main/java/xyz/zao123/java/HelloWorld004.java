package xyz.zao123.java;

/**
 * Java基础语法-Java变量
 * Java 中主要有如下3种类型的变量
 * 局部变量
 * 类变量（静态变量）
 * 成员变量（非静态变量）
 * @author gejt
 * @date 20200711
 */
public class HelloWorld004 {

    private int age = 1;//成员变量（非静态变量）

    private static String name = "gejt";//类变量（静态变量）

    public void  sayHello(){
        String message = "hello world";//局部变量
    }

}


