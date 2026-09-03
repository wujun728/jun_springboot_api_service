package com.zapi.util.sign.pdf.sm2;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.element.Image;
import com.zapi.itext7Sign.*;
import com.zapi.itext7Sign.PdfSignatureAppearance.RenderingMode;
import com.zapi.util.cert.sm2.CertificateUtils;
import com.zapi.util.file.FileUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/***
 * SM2盖章
 * @author zhonglz
 * 2023年4月28日10:42:52
 */
public class SignSM2Pdf {
	
	static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	public static void main(String[] args) throws Exception {
		
		// 需要盖章的pdf文件路径
		String pdfSignFile = "C:\\Users\\Administrator\\Desktop\\证书\\1.pdf";
		// 盖章后生产的pdf文件路径
		String pdfSignFileBreak = "C:\\Users\\Administrator\\Desktop\\证书\\盖章后1.pdf";
		// 印章路径
		String  sealSrc = "C:\\Users\\Administrator\\Desktop\\证书\\1.png";
		//证书密码
		String privateKeyPwd = "123456";
		//私钥证书地址
	    String privateKeyPath = "C:\\Users\\Administrator\\Desktop\\证书\\1.pfx";
	    
	    File file = new File(privateKeyPath);
		byte[] filebyte = FileUtils.file2byte(file);
		X509Certificate x509Certificate = CertificateUtils.getX509CertificateFromPfx(filebyte, privateKeyPwd);
		BCECPrivateKey bcecPrivateKey = CertificateUtils.getBCECPrivateKeyFromPfx(filebyte, privateKeyPwd);
		Certificate[] chain =new Certificate[]{x509Certificate};
	    
		// new一个上边自定义的方法对象，调用签名方法
		SignSM2Pdf app = new SignSM2Pdf();
		app.sign(pdfSignFile, String.format(pdfSignFileBreak, 1), chain, bcecPrivateKey, "签名原因", " 签名地点",sealSrc);
	}
	
	
	/***
	 * 
	 * @param src 需要签章的pdf文件路径
	 * @param dest 签完章的pdf文件路径
	 * @param chain  证书链
	 * @param pk 签名私钥
	 * @param reason	// 签名的原因，显示在pdf签名属性中，随便填
	 * @param location	// 签名的地点，显示在pdf签名属性中，随便填
	 * @param stamperSrc	//印章路径
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public void sign(String src, String dest , Certificate[] chain ,
			PrivateKey pk,
			String reason , String location,
			String stamperSrc
			) throws Exception {
		// 下边的步骤都是固定的，照着写就行了，没啥要解释的
		PdfReader reader = new PdfReader(src);
		// 目标文件输出流
		FileOutputStream os = new FileOutputStream(dest);
		// 创建签章工具PdfSigner ，最后一个boolean参数
		// false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
		// true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
		PdfSigner stamper = new PdfSigner(reader, os, true);
		// 获取数字签章属性对象，设定数字签章的属性
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setReason(reason);
		appearance.setLocation(location);
		ImageData img = ImageDataFactory.create(stamperSrc);
		// 读取图章图片，这个image是itext包的image
		Image image = new Image(img);
		float height = image.getImageHeight();
		float width = image.getImageWidth();
		// 设置签名的位置，页码，签名域名称，多次追加签名的时候，签名与名称不能一样
		// 签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
		// 四个参数的分别是，图章左下角x，图章左下角y，图章宽度，图章高度
		appearance.setPageNumber(1);
		appearance.setPageRect(new Rectangle(150, 500, width, height));
		// 插入盖章图片
		appearance.setSignatureGraphic(img);
		// 设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
		appearance.setRenderingMode(RenderingMode.GRAPHIC);
		// 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
		// 摘要算法
		IExternalDigest digest = new BouncyCastleDigest();
		// 签名算法
		IExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SM3, BouncyCastleProvider.PROVIDER_NAME);
		// 调用itext签名方法完成pdf签章
		stamper.setCertificationLevel(1);
		stamper.signDetached(digest, signature, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);
		os.close();
		reader.close();
		
	}

}
