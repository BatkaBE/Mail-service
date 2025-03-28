package com.example.fileManager.service;

import com.example.fileManager.dto.EmailRequest;
import com.example.fileManager.repository.EmailRequestRepository;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Object;
import jakarta.mail.util.ByteArrayDataSource;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.mail.MessagingException;
import java.io.InputStream;
import java.util.List;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRequestRepository emailRequestRepository;
    private final JavaMailSender javaMailSender;
    private final S3Service s3Service;

    public EmailRequest saveEmailRequest(EmailRequest emailRequest) {
        return emailRequestRepository.save(emailRequest);
    }

    public void sendShrekEmailWithAttachments(String to, String subject, String text) throws MessagingException, IOException, jakarta.mail.MessagingException {
        List<S3Object> files = s3Service.listFilesFromS3();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("batkabata42@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);


        String cssStyles = "<style>" +
                "body { font-family: 'Comic Sans MS', sans-serif; background-color: #90EE90; text-align: center; " +
                "background-image: url('https://geekculture.co/wp-content/uploads/2024/07/Feature-Shrek-5-1.jpg);" +
                "background-size: cover; color: #ffffff; padding: 50px; }" +
                "h1 { color: #FFD700; text-shadow: 3px 3px #006400; font-size: 36px; }" +
                "p { font-size: 20px; color: #ffffff; font-weight: bold; background: rgba(0,0,0,0.5); padding: 10px; display: inline-block; }" +
                ".shrek-box {\n" +
                "    background-image: url('https://static1.srcdn.com/wordpress/wp-content/uploads/2021/05/Shrek-and-swamp-Shrek-best-character.jpg?q=50&fit=crop&w=825&dpr=1.5');\n" +
                "    background-size: cover;" +
                "    background-position: center;" +
                "    border: 5px dashed #FFD700;\n" +
                "    padding: 15px;\n" +
                "    margin: 15px;\n" +
                "    border-radius: 10px;\n" +
                "    color: white;\n" +
                "    text-align: center;\n" +
                "    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5); \n" +
                "}" +
                "</style>";

        String shrekMessage = "<div class='shrek-box'>" +
                "<h1>Намагт тавтай морил</h1>" +
                "</div>";

        StringBuilder imgTags = new StringBuilder();
        for (S3Object s3Object : files) {
            String contentId = s3Object.key().replaceAll("[^a-zA-Z0-9]", "");
            imgTags.append("<div class='shrek-box'>")
                    .append("<img src='cid:").append(contentId).append("' style='max-width: 400px; border: 5px solid #FFD700; border-radius: 10px;'><br>")
                    .append("</div>");
        }

        String htmlContent = "<html><head>" + cssStyles + "</head><body>" + shrekMessage + text + "<br>" + imgTags + "</body></html>";


        messageHelper.setText(htmlContent, true);

        for (S3Object s3Object : files) {
            InputStream fileInputStream = s3Service.downloadFile(s3Object.key());
            DataSource dataSource = new ByteArrayDataSource(fileInputStream, "image/png"); // MIME төрлийг тохируулна
            String contentId = s3Object.key().replaceAll("[^a-zA-Z0-9]", ""); // CID-тэй тааруулсан нэр
            messageHelper.addInline(contentId, dataSource);
        }

        javaMailSender.send(mimeMessage);
    }

}