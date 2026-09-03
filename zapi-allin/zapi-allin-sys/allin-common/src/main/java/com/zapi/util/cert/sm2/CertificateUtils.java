package com.zapi.util.cert.sm2;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.*;
import org.bouncycastle.pkcs.bc.BcPKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS12PBEOutputEncryptorBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEInputDecryptorProviderBuilder;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class CertificateUtils {
	
	// 获取x509证书中的公钥对象
	public static BCECPublicKey getBCECPublicKey(X509Certificate x509Certificate) {
		// 获取证书公钥对象
		ECPublicKey ecPublicKey = (ECPublicKey) x509Certificate.getPublicKey();
        // 获取公钥算法g点
		ECPoint point = ecPublicKey.getQ();
		// 生成算法描述对象
        ECParameterSpec parameterSpec = new ECParameterSpec(Sm2Utils.curve, Sm2Utils.point,
        		Sm2Utils.n, Sm2Utils.h);
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, parameterSpec);
        return new BCECPublicKey(ecPublicKey.getAlgorithm(), pubKeySpec,
            BouncyCastleProvider.CONFIGURATION);
	}
	
	// 获取x509证书中的私钥对象
	public static BCECPrivateKey getBCECPrivateKeyFromPfx(byte[] pfxDer,String passWord) throws Exception {
		InputDecryptorProvider inputDecryptorProvider = //
				new JcePKCSPBEInputDecryptorProviderBuilder()
				.setProvider(BouncyCastleProvider.PROVIDER_NAME).build(passWord.toCharArray());
		// p12证书对象
		PKCS12PfxPdu pkcs12PfxPdu = new PKCS12PfxPdu(pfxDer);
		// 获取证书内容 def编码信息
		ContentInfo[] contentInfoArray = pkcs12PfxPdu.getContentInfos();
		
		if (contentInfoArray.length != 2) {
            throw new Exception("Only support one pair ContentInfo");
        }
		
		// 获取证书信息
		for (int i = 0; i != contentInfoArray.length; i++) {
			if (!contentInfoArray[i].getContentType().equals(PKCSObjectIdentifiers.encryptedData)) {
				PKCS12SafeBagFactory dataFact = new PKCS12SafeBagFactory(//
						contentInfoArray[i]);
	            PKCS12SafeBag[] bags = dataFact.getSafeBags();
	            // 解析出pfx中的私钥信息
	            PKCS8EncryptedPrivateKeyInfo encInfo = (PKCS8EncryptedPrivateKeyInfo) bags[0].getBagValue();
                PrivateKeyInfo info = encInfo.decryptPrivateKeyInfo(inputDecryptorProvider);
                PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(info.getEncoded());
                KeyFactory kf = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
                // 私钥对象生成
                BCECPrivateKey privateKey = (BCECPrivateKey) kf.generatePrivate(peks);
	            return privateKey;
			}
		}
		throw new Exception("Not found Private Key in this pfx");
	}
	
	// pfx证书解析
	public static X509Certificate getX509CertificateFromPfx(byte[] pfxDer,String passWord) throws Exception{
		InputDecryptorProvider inputDecryptorProvider = //
				new JcePKCSPBEInputDecryptorProviderBuilder()
				.setProvider(BouncyCastleProvider.PROVIDER_NAME).build(passWord.toCharArray());
		// p12证书对象
		PKCS12PfxPdu pkcs12PfxPdu = new PKCS12PfxPdu(pfxDer);
		// 获取证书内容 def编码信息
		ContentInfo[] contentInfoArray = pkcs12PfxPdu.getContentInfos();
		
		if (contentInfoArray.length != 2) {
            throw new Exception("Only support one pair ContentInfo");
        }
		
		// 获取证书信息
		for (int i = 0; i != contentInfoArray.length; i++) {
			if (contentInfoArray[i].getContentType().equals(PKCSObjectIdentifiers.encryptedData)) {
				PKCS12SafeBagFactory dataFact;
				try {
					dataFact = new PKCS12SafeBagFactory(//
							contentInfoArray[i], inputDecryptorProvider);
				} catch (PKCSException e) {
					//e.printStackTrace();
					throw new PKCSException("Bad PassWord");
				}
	            PKCS12SafeBag[] bags = dataFact.getSafeBags();
	            // 解析出pfx中的x509证书信息
	            X509CertificateHolder certHoler = (X509CertificateHolder) bags[0].getBagValue();
	            ByteArrayInputStream baos = new ByteArrayInputStream(certHoler.getEncoded());
	            // 生成x509证书
	            CertificateFactory cf = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
	            return (X509Certificate) cf.generateCertificate(baos);
        	
			}
		}
		throw new Exception("Not found X509Certificate in this pfx");
	}
	
	// 密钥转换
	public static BCECPublicKey convertX509ToECPublicKey(SubjectPublicKeyInfo subPubInfo) throws Exception {
		X509EncodedKeySpec eks = new X509EncodedKeySpec(subPubInfo.toASN1Primitive().getEncoded(ASN1Encoding.DER));
		KeyFactory kf = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPublicKey) kf.generatePublic(eks);
	}
	

	// 获取签发者信息
	public static X500Name generateIssuerX500Name() {
		X500NameBuilder issuerX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        issuerX500NameBuilder.addRDN(BCStyle.C, "CN");
        issuerX500NameBuilder.addRDN(BCStyle.O, "知录API");
        issuerX500NameBuilder.addRDN(BCStyle.OU, "ZHONGLZ");
        issuerX500NameBuilder.addRDN(BCStyle.CN, "ZHONGLZ");
        X500Name issuerX500Name = issuerX500NameBuilder.build();
        return issuerX500Name;
	}

	public static X509Certificate makeCertificate(byte[] csr,long certExpire) throws Exception {
		// 是不是CA机构证书
        //boolean isCA = false;
        // 签发证书有效期20年
        certExpire = 1L * certExpire * 24 * 60 * 60 * 1000;
        
        // 签发者公私钥
        KeyPair issuerKeyPair = Sm2Utils.generateKeyPair();
        PrivateKey issuerPrivateKey = issuerKeyPair.getPrivate();
        PublicKey issuerPublicKey = issuerKeyPair.getPublic();
        
        // 密钥用途KeyUsage.digitalSignature | KeyUsage.dataEncipherment
        KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.nonRepudiation);
		
		// 获取签发者信息
        X500Name issuerX500Name = CertificateUtils.generateIssuerX500Name();
        
        // 证书请求
        PKCS10CertificationRequest pkcs10CertificationRequest = new PKCS10CertificationRequest(csr);
		
        // 密钥转换
        PublicKey bcecPublicKey = convertX509ToECPublicKey(pkcs10CertificationRequest.getSubjectPublicKeyInfo());
        
        // 序列号
        BigInteger snAllocator = new BigInteger("1234567812345678");
        
        //JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
        // 使用使用者证书公钥生成x509证书对象
        X509v3CertificateBuilder v3CertGen = new JcaX509v3CertificateBuilder(
        		issuerX500Name, snAllocator,//
                new Date(System.currentTimeMillis()), //
                new Date(System.currentTimeMillis() + certExpire),//
                pkcs10CertificationRequest.getSubject(), bcecPublicKey);
        // 添加扩展信息
        v3CertGen.addExtension(Extension.keyUsage, true, keyUsage);
        
        // 证书签名算法
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SM3withSM2");
        contentSignerBuilder.setProvider(BouncyCastleProvider.PROVIDER_NAME);
        
        // 证书内容签名
        X509Certificate x509Certificate = new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(v3CertGen.build(contentSignerBuilder.build(issuerPrivateKey)));
        x509Certificate.checkValidity(new Date());
        x509Certificate.verify(issuerPublicKey);
        
        return x509Certificate;
	}


	// pfx证书生成
    public static PKCS12PfxPdu makePfx(PrivateKey privateKey, PublicKey publicKey, X509Certificate cert, String password)
            throws Exception {
        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

        PKCS12SafeBagBuilder eeCertBagBuilder = new JcaPKCS12SafeBagBuilder(cert);
        // eeCertBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
        //new DERBMPString("User Key"));
        eeCertBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId,
                extUtils.createSubjectKeyIdentifier(publicKey));

        char[] passwdChars = password.toCharArray();
        PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(privateKey,
                new BcPKCS12PBEOutputEncryptorBuilder(
                        PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC,
                        new CBCBlockCipher(new DESedeEngine())).build(passwdChars));
        //keyBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName,new DERBMPString("User Key"));
        keyBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId,
                extUtils.createSubjectKeyIdentifier(publicKey));

        PKCS12PfxPduBuilder pfxPduBuilder = new PKCS12PfxPduBuilder();
        PKCS12SafeBag[] certs = new PKCS12SafeBag[1];
        certs[0] = eeCertBagBuilder.build();
        pfxPduBuilder.addEncryptedData(new BcPKCS12PBEOutputEncryptorBuilder(
                        PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC,
                        new CBCBlockCipher(new RC2Engine())).build(passwdChars),
                certs);
        pfxPduBuilder.addData(keyBagBuilder.build());
        return pfxPduBuilder.build(new BcPKCS12MacCalculatorBuilder(), passwdChars);
    }

}
