package com.syed.code.services.email;

import com.syed.code.entities.email.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void sendEmail(Email email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setFrom(email.getFrom());
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setText(getContentFromTemplate(email), true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getContentFromTemplate(Email email) {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate(email.getTemplateName()), email.getContentMap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
