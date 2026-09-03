package com.zapi.util.sign.pdf;

import com.itextpdf.io.codec.Base64;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfName;
import com.zapi.itext7Sign.DigestAlgorithms;
import com.zapi.util.cert.sm2.CertificateUtils;
import com.zapi.util.file.FileUtils;
import com.zapi.util.sign.pdf.external.ExternalSignUtils;
import com.zapi.util.sign.pdf.external.SignService;
import com.zapi.util.sign.pdf.external.container.EmbdedSignContainer;
import com.zapi.util.sign.pdf.external.container.ExternalEmptySignContainer;
import com.zapi.util.sign.pdf.external.model.SignInfo;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.util.io.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;


/***
 * 测试分离式签名
 * @author zhonglz
 *
 */
public class ExternalSignPdf {
	 public static final String FOLDER = "C:\\Users\\Administrator\\Desktop\\测试\\";
	 public static final String SOURCEFILE = FOLDER + "1.pdf";
	 public static final String TEMPFILE = FOLDER  + "sign\\_empty.pdf";//预定位签名位置
	 public static final String RESULTFILE =  FOLDER + "sign\\_signed.pdf";//有效签名文件
	 public static final String singImg =  FOLDER + "1.png";
	 public static final String fieldNameA = "SIGNATURE_A";// 签名字段

	 public static final String privateKeyPath = FOLDER+"1.pfx";//私钥证书地址
	 public static final String privateKeyPwd = "123456";//证书密码
	 
	 public static String hashAlgorithm = DigestAlgorithms.SM3;//摘要算法，可修改
     //默认使用分离式签名，可修改
	 public static PdfName dicFilter = PdfName.Adobe_PPKLite;
	 public static PdfName dicSubfilter = PdfName.Adbe_pkcs7_detached;
	 
	public static void main(String[] args) throws Exception {
		
		byte[] filebyte = FileUtils.file2byte(new File(privateKeyPath));
		
		X509Certificate x509Certificate = CertificateUtils.getX509CertificateFromPfx(filebyte, privateKeyPwd);
		BCECPrivateKey bcecPrivateKey = CertificateUtils.getBCECPrivateKeyFromPfx(filebyte, privateKeyPwd);
		Certificate[] chain =new Certificate[]{x509Certificate};
		
		Rectangle rectangle = new Rectangle(350, 300, 200, 100);
	    SignInfo signInfo = new SignInfo(rectangle,1,"zapi签名","zapi签名",fieldNameA,singImg,true);
	    //获取空的签名文件
	    ExternalEmptySignContainer externalSignatureContainer = new ExternalEmptySignContainer(dicFilter,dicSubfilter,hashAlgorithm);
	    //初始化签名对象
	    ExternalSignUtils.initSign(Streams.readAll(new FileInputStream(SOURCEFILE)),TEMPFILE,signInfo,externalSignatureContainer);
	    //待签名哈希,发给外部服务器进行签名
        String toSignHash = Base64.encodeBytes(externalSignatureContainer.getDigestHash()) ;
        //签名后的哈希值
        String signedHash = SignService.sign(toSignHash,chain,bcecPrivateKey,hashAlgorithm);
        //验签，这一步需要通过外部服务器返回的signedHash，与原文件哈希值externalSignatureContainer.getDigestHash()进行验证
        byte[] cmsInfo = Base64.decode(signedHash);
        //将签名值打入文件完成分离签名
        ExternalSignUtils.embedSign(TEMPFILE,RESULTFILE,fieldNameA,new EmbdedSignContainer(cmsInfo));
        
	}

}
