package com.zapi.util.sign.pdf.external;

import com.itextpdf.io.codec.Base64;
import com.zapi.itext7Sign.BouncyCastleDigest;
import com.zapi.itext7Sign.IExternalDigest;
import com.zapi.itext7Sign.PdfPKCS7;
import com.zapi.itext7Sign.PdfSigner;
import com.zapi.util.cert.sm2.Sm2Utils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;

import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;

/***
 * 签名服务工具类
 * @author zhonglz
 *
 */
public class SignService {

	/***
	 * sm2签名
	 * @param toSignHash 待签名哈希
	 * @param chain  证书链
	 * @param pk 签名私钥
	 * @param hashAlgorithm 摘要算法
	 * @return
	 * @throws Exception
	 */
	public static String sign(String toSignHash,Certificate[] chain ,BCECPrivateKey prikey,String hashAlgorithm) throws Exception {
		byte[] hash = Base64.decode(toSignHash);
		IExternalDigest digest = new BouncyCastleDigest();
		// 1.itext的 PdfPKCS7工具类 先将待签名哈希值当做签名原文先去做一层SHA256加密后 得到 下面的 sh
		PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, digest, false);
        byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, PdfSigner.CryptoStandard.CMS, null, null);
       
        // 2. itext7内部签名工具类
        // IExternalSignature externalSignature = new PrivateKeySignature(prikey, hashAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        // byte[] extSignature = externalSignature.sign(sh);
        
        // 2.自定义sm2证书签名类 将sh当做签名原文进行签名
        byte[] extSignature= Sm2Utils.sign(prikey,sh);
        String algorithm = prikey.getAlgorithm();
        if (algorithm.equals("EC")) {
            algorithm = "ECDSA";
        }
        // 3.将签名后的值进行 序列化成pkcs7 
        sgn.setExternalDigest(extSignature, null, algorithm);
        byte[] pck7 = sgn.getEncodedPKCS7(hash, PdfSigner.CryptoStandard.CMS, null, null, null);
        return Base64.encodeBytes(pck7);
  }
	
	/***
	 * rsa签名
	 * @param toSignHash 待签名哈希
	 * @param chain  证书链
	 * @param pk 签名私钥
	 * @param hashAlgorithm 摘要算法
	 * @return
	 * @throws Exception
	 */
	public static String sign(String toSignHash,Certificate[] chain ,PrivateKey prikey,String hashAlgorithm) throws Exception {
		byte[] hash = Base64.decode(toSignHash);
		IExternalDigest digest = new BouncyCastleDigest();
		PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, digest, false);
        byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, PdfSigner.CryptoStandard.CMS, null, null);
       
        // itext7内部签名工具类
        // IExternalSignature externalSignature = new PrivateKeySignature(prikey, hashAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        // byte[] extSignature = externalSignature.sign(sh);
        
        //自定义RSA证书签名
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        //传入私钥初始化签名
        signature.initSign(prikey);
        //将数据添加到签名
        signature.update(sh);
        //计算签名
        byte[] extSignature = signature.sign();
        String algorithm = prikey.getAlgorithm();
        if (algorithm.equals("EC")) {
            algorithm = "ECDSA";
        }
        sgn.setExternalDigest(extSignature, null, algorithm);
        byte[] pck7 = sgn.getEncodedPKCS7(hash, PdfSigner.CryptoStandard.CMS, null, null, null);
        return Base64.encodeBytes(pck7);
  }
}
