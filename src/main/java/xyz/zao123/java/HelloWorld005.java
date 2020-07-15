package xyz.zao123.java;

import java.util.Arrays;

/**
 * Java基础语法-Java数组
 * 数组是储存在堆上的对象，可以保存多个同类型变量。
 * 语法 type[] 变量名 = new  type[数组大小];
 * 数组下标是从零开始的 例如：十个元素的整数数组  0的位置上放的是1元素 9的位置上放的是第10个元素
 * type[数组下标] 返回数组的第‘数组下标’对应的元素
 * 如果数组下标不在数组的范围内 会跑出处 @ArrayIndexOutOfBoundsException 此时程序出错
 * @author gejt
 * @date 20200711
 */
public class HelloWorld005 {

   public static void main(String[] args){

       //初始化十个元素的整数数组
       int[] array = new int[10];
       array[0] = 1;
       array[9] = 10;

       Arrays.stream(array).forEach(System.out::println);

       //初始化两个个元素的字符串数组
       String[] strArray = new String[2];
       strArray[0] = "hello ";
       strArray[1] = "world";

       Arrays.stream(strArray).forEach(System.out::println);
   }

}


