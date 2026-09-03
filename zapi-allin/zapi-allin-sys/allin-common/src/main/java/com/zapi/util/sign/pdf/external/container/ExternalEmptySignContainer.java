package com.zapi.util.sign.pdf.external.container;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.zapi.itext7Sign.BouncyCastleDigest;
import com.zapi.itext7Sign.DigestAlgorithms;
import com.zapi.itext7Sign.IExternalSignatureContainer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/***
 * 外部签名容器
 * @author zhonglz
 *
 */
public class ExternalEmptySignContainer implements IExternalSignatureContainer{
        private PdfDictionary sigDic;
        private String hashAlgorithm;
        private byte[] digestHash;
        public ExternalEmptySignContainer(PdfName filter, PdfName subFilter,String hashAlgorithm) {
            sigDic = new PdfDictionary();
            sigDic.put(PdfName.Filter, filter);
            sigDic.put(PdfName.SubFilter, subFilter);
            this.hashAlgorithm = hashAlgorithm;
        }
        @Override
        public byte[] sign(InputStream data) throws GeneralSecurityException {
            BouncyCastleDigest digest = new BouncyCastleDigest();
            try {
                this.digestHash= DigestAlgorithms.digest(data, digest.getMessageDigest(this.hashAlgorithm));
            } catch (IOException e) {
                throw new GeneralSecurityException("ExternalEmptySignatureContainer 签名异常",e);
            }
            return new byte[0];
        }


        @Override
        public void modifySigningDictionary(PdfDictionary signDic) {
            signDic.putAll(sigDic);
        }

    public byte[] getDigestHash() {
        return digestHash;
    }
}
