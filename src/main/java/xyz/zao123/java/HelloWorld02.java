package xyz.zao123.java;

/**
 * Java基础语法-Java修饰符
 * Java可以使用修饰符来修饰类中方法和属性。
 * 访问控制修饰符 : default, public , protected, private
 * 非访问控制修饰符 : final, abstract, static, synchronized
 * @author gejt
 * @date 20200711
 */
public class HelloWorld02 {

    //访问控制修饰符

    private int age = 1;
    protected String name = "hello world";
    public String gender = "Man";

    //非访问控制修饰符

    public static final String HELLO_MESSAGE = "hello world";

    public static synchronized void  sayHello(String name){
        System.out.println(name + ","+HELLO_MESSAGE);
    }

    public static  void main(String[] args){
        sayHello("gejt");
    }
}


