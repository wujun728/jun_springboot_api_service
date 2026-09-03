package com.zapi.util.sign.pdf.rsa;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.element.Image;
import com.zapi.itext7Sign.*;
import com.zapi.itext7Sign.PdfSignatureAppearance.RenderingMode;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class SignRSAPdf {
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/***
	 * 
	 * @param chain 证书链
	 * @param pk 签名私钥
	 * @param reason 签名的原因，显示在pdf签名属性中，随便填
	 * @param location 签名的地点，显示在pdf签名属性中，随便填
	 * @param signFrontFile 需要签署的文件
	 * @param signBackFilePath 签署后的文件路径
	 * @param sealFile 印章文件
	 * @param signPage 签署页码
	 * @param signPageX 签署位置 x轴
	 * @param signPageY 签署位置 Y轴
	 * @throws Exception
	 */
	public void sign(Certificate[] chain ,PrivateKey pk,String reason , String location,
			byte[] signFrontFile,String signBackFilePath,
			byte[] sealFile,Integer signPage,BigDecimal signPageX,BigDecimal signPageY,BigDecimal pdfH,BigDecimal pdfW)throws Exception {
				// 下边的步骤都是固定的，照着写就行了，没啥要解释的
				PdfReader reader = new PdfReader(new ByteArrayInputStream(signFrontFile));
				// 目标文件输出流
				FileOutputStream os = new FileOutputStream(signBackFilePath);
				// 创建签章工具PdfSigner ，最后一个boolean参数
				// false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
				// true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
				PdfSigner stamper = new PdfSigner(reader, os, true);
				// 获取数字签章属性对象，设定数字签章的属性
				PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
				appearance.setReason(reason);
				appearance.setLocation(location);
				ImageData img = ImageDataFactory.create(sealFile);
				// 读取图章图片，这个image是itext包的image
				Image image = new Image(img);
				// float height = image.getImageHeight();
				// float width = image.getImageWidth();
				// 设置签名的位置，页码，签名域名称，多次追加签名的时候，签名与名称不能一样
				appearance.setPageNumber(signPage);
				/****
				 * 签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
				 * setPageRect 四个参数的分别是，图章左下角x，图章左下角y，图章宽度，图章高度
				 * 
				 * 页面的图片大小左右内边距各为10px，所以这里x轴需要向左偏移10px，y轴之所以加100px，
				 * 是由于印章的范围区域一共为100px,页面计算印章位置是左上角开始算的，而itext是从左下角开始计算，
				 * 所以需要把印章的区域大小也要计算进去，
				 * 页面上图片实际大小是80px*80px，所以这里最后两项参数直接填写80px，也可以由前端传递过来
				 */
				//BigDecimal转float保留两位小数  xxx.setScale(2).floatValue()
				Rectangle rectangle =new Rectangle(signPageX.setScale(2, BigDecimal.ROUND_UP).floatValue()+10,signPageY.setScale(2, BigDecimal.ROUND_UP).floatValue()-100, 80, 80);
				appearance.setPageRect(rectangle);
				// 插入盖章图片
				appearance.setSignatureGraphic(img);
				// 设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
				appearance.setRenderingMode(RenderingMode.GRAPHIC);
				// 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
				// 摘要算法
				IExternalDigest digest = new BouncyCastleDigest();
				// 签名算法
				IExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, BouncyCastleProvider.PROVIDER_NAME);
				// 调用itext签名方法完成pdf签章
				stamper.setCertificationLevel(1);
				stamper.signDetached(digest, signature, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);
				os.close();
				// document.close();
				reader.close();
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
		appearance.setPageRect(new Rectangle(350, 200, width, height));
		// 插入盖章图片
		appearance.setSignatureGraphic(img);
		// 设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
		appearance.setRenderingMode(RenderingMode.GRAPHIC);
		// 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
		// 摘要算法
		IExternalDigest digest = new BouncyCastleDigest();
		// 签名算法
		IExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, BouncyCastleProvider.PROVIDER_NAME);
		// 调用itext签名方法完成pdf签章
		stamper.setCertificationLevel(1);
		stamper.signDetached(digest, signature, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);
		
		os.close();
		reader.close();
		
	}

	
	
	public static void main(String[] args) {
		// 需要盖章的pdf文件路径
		 String SRC = "C:\\Users\\Administrator\\Desktop\\证书1\\1.pdf";
		// 盖章后生产的pdf文件路径
		 String DEST = "C:\\Users\\Administrator\\Desktop\\证书1\\盖章后1.pdf";
		// 印章路径
		 String stamperSrc = "C:\\Users\\Administrator\\Desktop\\证书1\\1.png";
		 
		//证书密码
		String privateKeyPwd = "123456";
		 //私钥证书地址
	     String privateKeyPath = "C:\\Users\\Administrator\\Desktop\\证书1\\1.p12";
	     //公钥证书地址
	     // String publicKeyPath = "/Users/xiaozhong/Desktop/test/.cer";
		 try {
			// 获取证书的私钥
			// 获取JKS 服务器私有证书的私钥，取得标准的JKS的 KeyStore实例
            KeyStore ks = KeyStore.getInstance("JKS");
            // jks文件密码，根据实际情况修改
            ks.load(new FileInputStream(new File(privateKeyPath)), privateKeyPwd.toCharArray());
            String alias = (String) ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias,privateKeyPwd.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
			
			// new一个上边自定义的方法对象，调用签名方法
			SignRSAPdf app = new SignRSAPdf();
			
			app.sign(SRC, String.format(DEST, 1), chain, pk, "签名原因", " 签名地点",stamperSrc);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

}
