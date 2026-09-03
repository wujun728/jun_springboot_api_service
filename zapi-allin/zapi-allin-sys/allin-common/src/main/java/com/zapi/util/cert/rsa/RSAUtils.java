package com.zapi.util.cert.rsa;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;


/***
 * RSA证书生成工具类
 * @author xiaozhong
 *
 */
public class RSAUtils {

	/***
	 * 密钥对 生成器
	 * @param certNum 证书位数
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static KeyPair getKey(int certNum) throws NoSuchAlgorithmException {
		// 密钥对 生成器，RSA算法 生成的  提供者是 BouncyCastle
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",  new BouncyCastleProvider());
		 // 密钥长度 1024
		generator.initialize(certNum); 
		// 证书中的密钥 公钥和私钥
		KeyPair keyPair = generator.generateKeyPair();
		return keyPair;
	}
 
	/**
	 * RSA证书生成
	 * @param password  密码   
	 * @param issuerStr 颁发机构信息
	 * @param subjectStr 使用者信息
	 * @param certificateCRL 颁发地址
	 * @param certExpire 证书有效期 （单位天）
	 * @param certNum 证书位数：一般1024或2048
	 * @return
	 */
	public static Map<String, byte[]> createCert(String password, String issuerStr, String subjectStr, String certificateCRL,long certExpire,int certNum) {
 
		Map<String, byte[]> result = new HashMap<String, byte[]>();
		ByteArrayOutputStream out = null;
		try {
			//  生成JKS证书
			//  KeyStore keyStore = KeyStore.getInstance("JKS");
			//  标志生成PKCS12证书
			KeyStore keyStore = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
			keyStore.load(null, null);
			KeyPair keyPair = getKey(certNum);
			//  issuer与 subject相同的证书就是CA证书
			Certificate cert = generateCertificateV3(issuerStr, subjectStr,  keyPair, result, certificateCRL, null,certExpire);
			// cretkey随便写，标识别名
			keyStore.setKeyEntry("知录API信息分享中心",  keyPair.getPrivate(),  password.toCharArray(),  new Certificate[] { cert });
			out = new ByteArrayOutputStream();
			cert.verify(keyPair.getPublic());
			keyStore.store(out, password.toCharArray());
			byte[] keyStoreData = out.toByteArray();
			result.put("keyStoreData", keyStoreData);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}
 
	/**
	 * @param issuerStr
	 * @param subjectStr
	 * @param keyPair
	 * @param result
	 * @param certificateCRL
	 * @param extensions
	 * @return
	 */
	public static Certificate generateCertificateV3(String issuerStr, String subjectStr, KeyPair keyPair, Map<String, byte[]> result,
			String certificateCRL, List<Extension> extensions,long certExpire) {
 
		ByteArrayInputStream bout = null;
		X509Certificate cert = null;
		try {
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			Date notBefore = new Date();
			
			certExpire = 1L * certExpire * 24 * 60 * 60 * 1000;
//			Calendar rightNow = Calendar.getInstance();
//			rightNow.setTime(notBefore);
//			// 日期加1年
//			rightNow.add(Calendar.YEAR, 1);
//			Date notAfter = rightNow.getTime();
			Date notAfter = new Date(System.currentTimeMillis() + certExpire);
			// 证书序列号
			BigInteger serial = BigInteger.probablePrime(256, new Random());
			X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
					new X500Name(issuerStr), serial, notBefore, notAfter,new X500Name(subjectStr), publicKey);
			JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder( "SHA1withRSA");//SHA256withRSA
			SecureRandom secureRandom = new SecureRandom();
			jBuilder.setSecureRandom(secureRandom);
			ContentSigner singer = jBuilder.setProvider(  new BouncyCastleProvider()).build(privateKey);
			// 分发点
			ASN1ObjectIdentifier cRLDistributionPoints = new ASN1ObjectIdentifier( "2.5.29.31");
			GeneralName generalName = new GeneralName( GeneralName.uniformResourceIdentifier, certificateCRL);
			GeneralNames seneralNames = new GeneralNames(generalName);
			DistributionPointName distributionPoint = new DistributionPointName( seneralNames);
			DistributionPoint[] points = new DistributionPoint[1];
			points[0] = new DistributionPoint(distributionPoint, null, null);
			CRLDistPoint cRLDistPoint = new CRLDistPoint(points);
			builder.addExtension(cRLDistributionPoints, true, cRLDistPoint);
			// 用途
			ASN1ObjectIdentifier keyUsage = new ASN1ObjectIdentifier( "1.3.14.3.2.26");
			// | KeyUsage.nonRepudiation | KeyUsage.keyCertSign
			builder.addExtension(keyUsage, true, new KeyUsage( KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
			// 基本限制 X509Extension.java
			ASN1ObjectIdentifier basicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
			builder.addExtension(basicConstraints, true, new BasicConstraints(true));
			// privKey:使用自己的私钥进行签名，CA证书
			if (extensions != null){
				for (Extension ext : extensions) {
					builder.addExtension(
							new ASN1ObjectIdentifier(ext.getOid()),
							ext.isCritical(),
							ASN1Primitive.fromByteArray(ext.getValue()));
				}
			}
			X509CertificateHolder holder = builder.build(singer);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			bout = new ByteArrayInputStream(holder.toASN1Structure() .getEncoded());
			cert = (X509Certificate) cf.generateCertificate(bout);
			byte[] certBuf = holder.getEncoded();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 证书数据
			result.put("certificateData", certBuf);
			//公钥
			result.put("publicKey", publicKey.getEncoded());
			//私钥
			result.put("privateKey", privateKey.getEncoded());
			//证书有效开始时间
			result.put("notBefore", format.format(notBefore).getBytes("utf-8"));
			//证书有效结束时间
			result.put("notAfter", format.format(notAfter).getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (IOException e) {
				}
			}
		}
		return cert;
	}
 
	public static void main(String[] args) throws Exception{
		
		/***
		 * 测试使用main生成证书 需要在jre1.8.0_191\lib\security\java.security  中加入一行代码
		 * security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider
		 */
		String fileUrl =  "C:\\Users\\Administrator\\Desktop\\证书\\";

		// CN: 名字与姓氏    OU : 组织单位名称
		// O ：组织名称  L : 城市或区域名称  E : 电子邮件
		// ST: 州或省份名称  C: 单位的两字母国家代码 
		
		//颁发者固定写死
		String issuerStr = "CN=qjs,OU=qjs,O=知录,C=CN,E=1260649342@qq.com,L=长沙,ST=湖南";
		//使用者，需自定义传参带入
		String subjectStr = "CN=qjs,OU=qjs,O=知录,C=CN,E=1260649342@qq.com,L=长沙,ST=湖南";
		//颁发地址，后续用到什么项目，公司地址等
		String certificateCRL  = "https://www.baidu.com";
		//证书密码
		String certPasswrod = "123456";
		//证书有效期 365天
		long certExpire = 365;
		//证书位数
		int certNum = 2048;
		
		Map<String, byte[]> result = createCert(certPasswrod, issuerStr, subjectStr, certificateCRL,certExpire,certNum);
		//以下生成两种格式证书
		FileOutputStream outPutStream = new FileOutputStream(fileUrl+"\\1.p12"); // p12 私钥证书
		outPutStream.write(result.get("keyStoreData"));
		outPutStream.close();
		
		FileOutputStream fos = new FileOutputStream(new File(fileUrl+"\\1.cer"));//cer公钥证书
		fos.write(result.get("certificateData"));
		fos.flush();
		fos.close();
	}
	
}
