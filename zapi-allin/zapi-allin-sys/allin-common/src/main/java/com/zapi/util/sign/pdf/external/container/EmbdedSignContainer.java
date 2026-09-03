package com.zapi.util.sign.pdf.external.container;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.zapi.itext7Sign.IExternalSignatureContainer;

import java.io.InputStream;
import java.security.GeneralSecurityException;

/***
 * 签名值
 * @author zhonglz
 *
 */
public class EmbdedSignContainer implements IExternalSignatureContainer {
    byte[] signature;
    public EmbdedSignContainer(byte[] pkcs7Signature) {
        this.signature = pkcs7Signature;
    }
    public byte[] sign(InputStream is) throws GeneralSecurityException {
        return signature;
    }
    public void modifySigningDictionary(PdfDictionary signDic) {
    }
}
