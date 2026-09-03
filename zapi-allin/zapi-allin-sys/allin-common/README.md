# allin-common 公共模块
## 概述
`allin-common` 是项目的公共工具类模块，包含所有模块都可以使用的通用组件、工具类和常量。
## 模块结构
```
allin-common/
├── src/main/java/com/zapi/
│   ├── common/          # 通用类
│   │   └── Result.java  # 统一响应结果类
│   ├── util/            # 工具类
│   │   ├── JwtUtil.java     # JWT工具类
│   │   ├── DateUtil.java    # 日期工具类
│   │   └── StringUtil.java  # 字符串工具类
│   └── aspect/          # 切面类
│       └── LogAspect.java   # 日志切面类
└── pom.xml
```
准备工作：
项目运行起来需要将resources中的 word.jar包引入项目中 ，这个包主要功能是word转pdf

证书生成：
sm2证书：com.zapi.util.cert.CertUtils.java里面有个main方法 修改一下相对应数据信息就好了
rsa证书：com.zapi.util.cert.rsa.RSAUtils.java里面有个main方法 修改一下相对应数据信息就好了

印章生成：
com.zapi.util.SealUtils.java里面有个main方法 修改一下相对应数据信息就好了

具体测试方法在com.zapi.util.. 目录下的 pdf包里面，
有测试类：
SignRSAPdf.java
SignSM2Pdf.java
以及 ExternalSignPdf.java测试类实现了分离式签名盖章

验签的阅读器可在福昕上验签：https://foxit.pdf365.cn/

这里没有直接引入itext7的sign包，而是将itext7中的源码包sign里面的所有文件
复制到了当前项目的com.zapi.itext7Sign目录下

然后修改部分国密SM2证书给pdf盖章处理,项目中的盖章直接用本地的方法就好了

基于BouncyCastle JAR包+itextpdf
利用itext提供的2个签名接口：
ExternalDigest 及 ExternalSignature，
修改BC部份代码实现国密SM2签名处理。
具体处理如下：
1. BouncyCastleDigest.java 增加
if(("1.2.156.10197.1.401").equals(oid)){
return new org.bouncycastle.jcajce.provider.digest.SM3.Digest();
}

2. DigestAlgorithms.java 增加
DIGEST_NAMES.put("1.2.156.10197.1.401",“SM3”);
ALLOWED_DIGESTS.put("SM3","1.2.156.10197.1.401");

3. EncryptionAlgorithms.java 增加
    ALGORITHM_NAMES.put("1.2.156.10197.1.501","SM2");

4. PrivateKeySignature.java 中 sign(byte[] b) 方法增加
   if("SM3".equalsIgnoreCase(this.hashAlgorithm)){
   signMode = "SM3withSM2";
   }
5. PdfPKCS7.java 中 setExternalDigest(...) 方法中 将 ECDSA 条件下的赋值改为
   this.digestEncryptionAlgorithmOid = "1.2.156.10197.1.501";
 

