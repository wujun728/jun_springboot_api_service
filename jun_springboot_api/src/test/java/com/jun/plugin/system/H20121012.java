package com.jun.plugin.system;


import java.io.BufferedInputStream;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileInputStream;

import java.io.InputStreamReader;

import java.io.Reader;

 

/**

 * @author 码农小江

 * H20121012.java

 * 2012-10-12下午11:40:21

 */

public class H20121012 {

    /**

     * 功能：Java读取txt文件的内容

     * 步骤：1：先获得文件句柄

     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取

     * 3：读取到输入流后，需要读取生成字节流

     * 4：一行一行的输出。readline()。

     * 备注：需要考虑的是异常情况

     * @param filePath

     */

    public static void readTxtFile(String filePath){

        try {

                String encoding="GBK";

                File file=new File(filePath);

                if(file.isFile() && file.exists()){ //判断文件是否存在

                    InputStreamReader read = new InputStreamReader(

                    new FileInputStream(file),encoding);//考虑到编码格式

                    BufferedReader bufferedReader = new BufferedReader(read);

                    String lineTxt = null;

                    while((lineTxt = bufferedReader.readLine()) != null){

                        System.out.println(lineTxt);

                    }

                    read.close();

        }else{

            System.out.println("找不到指定的文件");

        }

        } catch (Exception e) {

            System.out.println("读取文件内容出错");

            e.printStackTrace();

        }

     

    }

     

    public static void main(String argv[]){

        String filePath = "D:/dy.txt";

//      "res/";

        readTxtFile(filePath);

    }

     

     

 

}
