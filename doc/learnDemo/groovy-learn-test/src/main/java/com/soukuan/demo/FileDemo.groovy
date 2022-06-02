package com.soukuan.demo;

class FileDemo {

    static void testRead(){
        def index = 1;
        new File("E:/learnDemo/groovy-learn/src/demo/TestDemo.groovy").eachLine {
            line -> println "${index++} : $line";
        }

        File file = new File("E:/learnDemo/groovy-learn/src/demo/TestDemo.groovy")
        println file.text
    }

    static void testWriter(){
        new File('E:/','Example.txt').withWriter('utf-8') {
            writer -> writer.writeLine 'Hello World'
        }

        File file = new File('E:/','Example.txt');
        println "The file ${file.absolutePath} has ${file.length()} bytes"
        println "File? ${file.isFile()}"
        println "Directory? ${file.isDirectory()}"
    }

    static void main(String[] args) {
        testWriter()
    }
}
