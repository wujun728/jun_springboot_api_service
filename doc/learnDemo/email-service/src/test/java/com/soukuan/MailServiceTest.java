package com.soukuan;

import com.soukuan.component.EmailHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private TemplateEngine templateEngine;

    public static final String FROM_EMAIL = "872213765@qq.com";

    public static final String TO_EMAIL = "872213765@qq.com";

    @Test
    public void testSimpleMail() throws Exception {
        emailHelper.sendSimpleMail(FROM_EMAIL, TO_EMAIL, "test simple mail", " hello this is simple mail");
    }

    @Test
    public void testHtmlMail() throws Exception {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        emailHelper.sendHtmlMail(FROM_EMAIL, TO_EMAIL,"这是一封Html邮件",content);
    }

    @Test
    public void sendAttachmentsMail() {
        String filePath="e:\\tmp\\application.log";
        emailHelper.sendAttachmentsMail(FROM_EMAIL, TO_EMAIL, "主题：带附件的邮件", "有附件，请查收！", filePath);
    }

    @Test
    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = "C:\\Users\\summer\\Pictures\\favicon.png";
        emailHelper.sendInlineResourceMail(FROM_EMAIL, TO_EMAIL, "主题：这是有图片的邮件", content, imgPath, rscId);
    }

    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("emailTemplate", context);
        emailHelper.sendHtmlMail(FROM_EMAIL, TO_EMAIL,"主题：这是模板邮件",emailContent);
    }
}
