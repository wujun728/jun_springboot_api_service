package com.zapi.util.sign.pdf.external;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.zapi.itext7Sign.IExternalSignatureContainer;
import com.zapi.itext7Sign.PdfSignatureAppearance;
import com.zapi.itext7Sign.PdfSigner;
import com.zapi.util.sign.pdf.external.model.SignInfo;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Calendar;

/***
 * 分离式哈希签名
 * @author zhonglz
 *
 */
public class ExternalSignUtils{
    /**
     * 1.initSigner初始化签名对象；
     * 2.通过externalSignatureContainer获取digestHash，去cfca计算签名值；
     * 3.embedSignature，嵌入签名值。embedSignature可以独立和上面的步骤分离
     * @param pdfFile 源文件字节码
     * @param outPath 签名文件
     * @param externalSignatureContainer 签章实现
     * @return
     * @throws IOException
     */
    public static void initSign(byte[] pdfFile, String outPath,SignInfo signInfo,IExternalSignatureContainer externalSignatureContainer) throws Exception {
        boolean success = false;
        int estimatedSize = 8192;//300000;
        while (!success) {
            try {
                PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfFile));
                OutputStream output = new FileOutputStream(outPath);
                StampingProperties stampingProperties = new StampingProperties();
                if (signInfo.isAppendModel() == true) {
                    stampingProperties.useAppendMode();
                }
                PdfSigner signer = new PdfSigner(pdfReader, output, stampingProperties);
                PdfSignatureAppearance appearance = signer.getSignatureAppearance();
                appearance
                        .setPageRect(signInfo.getRectangle())
                        .setPageNumber(signInfo.getPageNumber())
                        .setSignatureGraphic(ImageDataFactory.create(signInfo.getImgPath()))
                        .setReason(signInfo.getReason())
                        .setLocation(signInfo.getLocation())
                        .setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
                signer.setFieldName(signInfo.getFieldName());
                signer.setSignDate(Calendar.getInstance());
                signer.signExternalContainer(externalSignatureContainer, estimatedSize);
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                estimatedSize += 1000;
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 嵌入分离式签名信息
     * @param readPath 已经进行空签章的文件
     * @param outPath 输出文件
     * @param fieldName 已经打入的签章字段名，必须和空签章打入的一致
     * @param external 签名
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void embedSign(String readPath, String outPath, String fieldName, IExternalSignatureContainer external) throws IOException, GeneralSecurityException {
        try( 
        	PdfReader reader = new PdfReader(readPath);
        	FileOutputStream os = new FileOutputStream(outPath)) 
	        {
	            PdfSigner signer = new PdfSigner(reader, os, new StampingProperties());
	            try {
	                // 在已预留空间的PDF文件上签名。该字段必须覆盖整个文档。
	                signer.signDeferred(signer.getDocument(), fieldName, os, external);
	            } catch (IOException e) {
	                throw new IOException("embedSign 签署异常",e);
	            } catch (GeneralSecurityException e) {
	                throw new GeneralSecurityException("embedSign 签署异常",e);
	            }
	        }
    }

}
