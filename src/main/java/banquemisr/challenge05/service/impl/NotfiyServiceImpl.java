package banquemisr.challenge05.service.impl;


import banquemisr.challenge05.config.BankProperties;
import banquemisr.challenge05.service.NotifyService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Bishoy Samir
 * Notfiy service to send email to users
 */
@Service
@Slf4j
public class NotfiyServiceImpl implements NotifyService {

    @Autowired
    private BankProperties bankProperties;

    private final JavaMailSender javaMailSender;

    public NotfiyServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMessage(String to, String tile, String content) {

        String from = bankProperties.getMail().getFrom();
        log.info("Send email to '{}' with subject '{}' and content={}", to, tile, content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, java.nio.charset.StandardCharsets.UTF_8.toString());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(tile);
            message.setText(content, false);
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User '{}'", to);
        } catch (Exception e) {
            log.error("could not send mail" + e.getMessage());
        }
        log.info("mail sent to: " + to);
    }
}
