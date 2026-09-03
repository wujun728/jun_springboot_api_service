package com.zapi.util.cert.rsa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 私钥签名，公钥验签
 * @author zhonglz
 *
 */
public class RSASignUtil {
    private static String CHARSET_ENCODING = "UTF-8";
    private static String ALGORITHM = "SHA256withRSA";
    
    /**
     * 签名
     * @param srcData 需要签名的摘要
     * @param privateKeyPath 私钥证书的文件路径
     * @param privateKeyPwd 证书密码
     * @return
     */
    public static String sign(String srcData, String privateKeyPath, String privateKeyPwd){
        if(srcData==null || privateKeyPath==null || privateKeyPwd==null){
            return "";
        }
        try {
            // 获取证书的私钥
            PrivateKey key = readPrivate(privateKeyPath, privateKeyPwd);
            // 进行签名服务
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(key);
            signature.update(srcData.getBytes(CHARSET_ENCODING));
            byte[] signedData = signature.sign();
            return Base64.getEncoder().encodeToString(signedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 签名
     * @param srcData 需要签名的摘要
     * @param certFileInputStream 私钥证书的文件流
     * @param privateKeyPwd 证书密码
     * @return
     */
    public static String sign(String srcData, InputStream certFileInputStream, String privateKeyPwd) {
        try {
            // 获取证书的私钥
            PrivateKey key = readPrivate(certFileInputStream, privateKeyPwd);
            // 进行签名服务
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(key);
            signature.update(srcData.getBytes(CHARSET_ENCODING));
            byte[] signedData = signature.sign();
            return Base64.getEncoder().encodeToString(signedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
	}

	/**
     * 验签
     * @param srcData 需要签名的摘要
     * @param signedData 签名后的值
     * @param publicKeyPath 公钥证书的文件路径
     * @return
     */
    public static boolean verify(String srcData, String signedData, String publicKeyPath){
        if(srcData==null || signedData==null || publicKeyPath==null){
            return false;
        }
        try {
            PublicKey publicKey = readPublic(publicKeyPath);
            Signature sign = Signature.getInstance(ALGORITHM);
            sign.initVerify(publicKey);
            sign.update(srcData.getBytes(CHARSET_ENCODING));
            return sign.verify(Base64.getDecoder().decode(signedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 验签
     * @param srcData 需要签名的摘要
     * @param signedData 签名后的值
     * @param publicKey 公钥证书的文件路径
     * @return
     */
    public static boolean verify(String srcData, String signedData, PublicKey publicKey){
        try {
            Signature sign = Signature.getInstance(ALGORITHM);
            sign.initVerify(publicKey);
            sign.update(srcData.getBytes(CHARSET_ENCODING));
            return sign.verify(Base64.getDecoder().decode(signedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * 验签
     * @param srcData 需要签名的摘要
     * @param signedData 签名后的值
     * @param publicKeyPath 公钥证书的文件路径
     * @return
     */
    public static boolean verify(String srcData, byte[] signedData, String publicKeyPath){
        if(srcData==null || signedData==null || publicKeyPath==null){
            return false;
        }
        try {
            PublicKey publicKey = readPublic(publicKeyPath);
            Signature sign = Signature.getInstance(ALGORITHM);
            sign.initVerify(publicKey);
            sign.update(srcData.getBytes(CHARSET_ENCODING));
            return sign.verify(signedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    

    /**
     * 读取公钥
     * @param publicKeyPath 公钥证书的地址
     * @return
     */
    public static PublicKey readPublic(String publicKeyPath){
        if(publicKeyPath==null){
            return null;
        }
        PublicKey pk = null;
        FileInputStream bais = null;
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            bais = new FileInputStream(publicKeyPath);
            X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(bais);
            pk = cert.getPublicKey();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pk;
    }
    
    /***
     *  读取私钥
     * @param certFileInputStream  私钥证书文件流
     * @param privateKeyPwd 证书密码
     * @return
     */
    private static PrivateKey readPrivate(InputStream certFileInputStream, String privateKeyPwd) {
    	 InputStream stream = null;
         try {
             // 获取JKS 服务器私有证书的私钥，取得标准的JKS的 KeyStore实例
             KeyStore store = KeyStore.getInstance("JKS");
             // jks文件密码，根据实际情况修改
             store.load(certFileInputStream, privateKeyPwd.toCharArray());
             // 获取jks证书别名
             Enumeration en = store.aliases();
             String pName = null;
             while (en.hasMoreElements()) {
                 String n = (String) en.nextElement();
                 if (store.isKeyEntry(n)) {
                     pName = n;
                 }
             }
             // 获取证书的私钥
             PrivateKey key = (PrivateKey) store.getKey(pName,
                     privateKeyPwd.toCharArray());
             return key;
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if(stream != null){
                 try {
                     stream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
         return null;
	}
    
   /***
    *  读取私钥
    * @param privateKeyPath  私钥证书地址
    * @param privateKeyPwd 证书密码
    * @return
    */
    public static PrivateKey readPrivate(String privateKeyPath, String privateKeyPwd){
        if(privateKeyPath==null || privateKeyPwd==null){
            return null;
        }
        InputStream stream = null;
        try {
            // 获取JKS 服务器私有证书的私钥，取得标准的JKS的 KeyStore实例
            KeyStore store = KeyStore.getInstance("JKS");
            stream = new FileInputStream(new File(privateKeyPath));
            // jks文件密码，根据实际情况修改
            store.load(stream, privateKeyPwd.toCharArray());
            // 获取jks证书别名
            Enumeration en = store.aliases();
            String pName = null;
            while (en.hasMoreElements()) {
                String n = (String) en.nextElement();
                if (store.isKeyEntry(n)) {
                    pName = n;
                }
            }
            // 获取证书的私钥
            PrivateKey key = (PrivateKey) store.getKey(pName,
                    privateKeyPwd.toCharArray());
            return key;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    
    //测试验证签名
    public void testSign() {
    	String srcData="zhonglz";
		String signedData="rA3/oJBzfT7DsVGezBrq7uEupWLH4y+I4HSP9cn6LkVB1IZSpnFZivKxyfcUZk4LZGyidQ2zaEwGfF+X+TWAVAxD/igASJQ0cAZmijWr3Yt5Uj4E7VfHnwkJfBd8+zm553xfLeeex/xJdEjECObZmvEgqi63ObL6fn6Oqf8M/2ZYUlTrBoupfSd7b2AD2hFbScVN0I0tu2D+9gbtrgZ4mwdZmvVtwnnRAAOFYjTZQPLnR5EoZ4k0HeozlgRrUO0PXPCjhe4EwTC+3r98i+COw8DYw32hlzJFXGYskmPWA2BBxcOj059MwD8UltS0enbAD1+qwuhyckypDhHv0ZAQ9g==";
		String KEYSTORE_FILE = "/Users/xiaozhong/Downloads/628e6c8cf0e04c3a8de1d3ae51e6922e.p12";
		String KEYSTORE_PASSWORD = "123456";
		final String KEYSTORE_ALIAS    = "alias";
		try
        {
            KeyStore ks = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
            FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")){
            	nPassword = null;
            }else{
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            Enumeration enums = ks.aliases();
            String keyAlias = null;
            if (enums.hasMoreElements()){
                keyAlias = (String)enums.nextElement();
            }
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            Signature sign = Signature.getInstance(ALGORITHM);
            sign.initVerify(pubkey);
            sign.update(srcData.getBytes(CHARSET_ENCODING));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
		
    	//需要签名的摘要
    	String originalText = "zhonglz";
    	//证书密码
		String certPwd = "123456";
		//私钥证书地址
    	String privateKeyPath = "/Users/xiaozhong/Downloads/628e6c8cf0e04c3a8de1d3ae51e6922e.p12";
    	//公钥证书地址
    	String publicKeyPath = "E:\\minio\\file\\azt\\test\\test5.cer";
    	
    	//签名后的值
    	String signedData = sign(originalText, privateKeyPath, certPwd);
//    	//验签
//    	boolean verifyBool = verify(originalText,signedData, publicKeyPath);
//    	//验签结果
	}

	
    
    
}
