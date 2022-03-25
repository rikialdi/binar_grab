package com.binar.grab.service.email.email;

import com.binar.grab.config.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
	private JavaMailSender sender;
//    @Autowired
//    private JavaMailSenderImpl sender;

    Config config2 = new Config();

    @Autowired
    private Configuration config;

    @Value("${spring.mail.username:}")//FILE_SHOW_RUL
    private String emailPengirim;

    public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment :naro logo di di../resources
            helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

//            13.	Download Gimage dari gambar kemudian simpan ke local
//            URL url = new URL("https://binar-test.herokuapp.com/api/showFile/2432022025921Captureass.PNG");//("http://google.com/pathtoaimage.jpg");
//            BufferedImage img = ImageIO.read(url);
//            File file = new File("./cdn/downloaded.jpg");
//            ImageIO.write(img, "jpg", file);
//            helper.addAttachment("image1", file);
            String fileName= "./cdn/Captureass.PNG";
            File file2 = new File(fileName);
            String formatFile = fileName.substring(fileName.lastIndexOf(".") );
            helper.addAttachment(config2.convertDateToString(new Date())+"image4"+formatFile, file2);


            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
//            helper.setTo(emailPengirim);
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            sender.send(message);

            response.setMessage("mail send to : " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }


}
