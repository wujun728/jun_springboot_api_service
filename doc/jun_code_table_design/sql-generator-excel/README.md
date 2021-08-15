# SqlGeneratorExcel

easypoi vs easyexcel vs poi
easypoi导入Excel最佳实践 - WeJan1 - 博客园
https://www.cnblogs.com/vcmq/p/12149673.html

利用阿里研发的easyexcel导入导出excel，避免OOM，并对excel加密保护_qq_40184563的博客-CSDN博客
https://blog.csdn.net/qq_40184563/article/details/102687723

解决javapoi海量数据导出内存溢出问题_一个摩羯座的程序猿-CSDN博客
https://blog.csdn.net/yiwangjiushixingfu/article/details/80258148

poi导致内存泄露分析_jackllvv的博客-CSDN博客
https://blog.csdn.net/asdasd3418/article/details/84850670

#### 介绍
数据库文档生成工具，根据数据库存在的表 反解析成Excel数据库文档

#### 前言
能够根据配置的数据库信息,反向根据数据库生成指定的数据库的数据库文档Excel格式的文件。

datasource.properties 的配置项有：
````
#数据库的IP
host=127.0.0.1
#数据库的端口号
port=3306
#数据库的名称
databaseName=test
#数据库账号
username=root
#数据库密码
password=root
#导出Excel的绝对路径
exportPath=D://Soft
````

#### 使用说明

##### 1. 自行构建方式
1. 拉代码
2. maven安装依赖
3. 编译下或者运行下为了生成``target``文件
4. 复制根目录下的``datasource.properties``文件复制到``target``文件里
5. 配置``datasource.properties``里的参数为你自己的
6. 运行Main方法
7. 查看自动生成的 Excel 数据库文档文件

##### 2. 使用构建好的发行版
1. 下载构建好的压缩包文件：https://gitee.com/Jack-chendeng/sql-generator-excel/raw/master/packages/SqlGeneratorExcel-hotfix20210603.jar
2. 解压
3. 配置``datasource.properties``里的参数为你自己的
4. 打开命令行窗口，输入命令 java -jar SqlGeneratorExcel-xxx-xxx.jar
5. 查看自动生成的 Excel 数据库文档文件

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
