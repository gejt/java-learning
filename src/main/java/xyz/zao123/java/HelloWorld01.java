package xyz.zao123.java;

/**
 * Java基础语法-Java标识符
 * Java所有的组成部分都需要名字。类名、变量名以及方法名都被称为标识符。
 * 所有的标识符都应该以字母（A-Z 或者 a-z）,美元符（$）、或者下划线（_）开始
 * 首字符之后可以是字母（A-Z 或者 a-z）,美元符（$）、下划线（_）或数字的任何字符组合
 * 关键字不能用作标识符
 * 标识符是大小写敏感的
 * @author gejt
 * @date 20200711
 */
public class HelloWorld01 {

    public static void main(String[] args) {

        //合法的标识符
        int age = 1;
        int $salary = 100;
        String value = "hello world";
        String __1_str_value = "hello world 2!";

        //非法的标识符
        //int 123age = 1;
        //int -salary = 100;

    }
}


