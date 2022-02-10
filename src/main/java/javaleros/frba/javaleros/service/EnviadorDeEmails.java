package javaleros.frba.javaleros.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
@Log
public class EnviadorDeEmails {

  @Autowired
  private JavaMailSender enviadorMails;

  public void enviarMail(String para, String asunto, String cuerpo) {
    log.info("Enviando mail...");

    try {
      enviarMailConAdjunto(para, asunto, cuerpo);
    } catch(MessagingException | IOException e) {
      e.printStackTrace();
    }

    log.info("Email sent.");
  }

  private void enviarMailConAdjunto(String para, String asunto, String cuerpo) throws MessagingException, IOException {

    MimeMessage mail = enviadorMails.createMimeMessage();

    // true = multipart message
    MimeMessageHelper helper = new MimeMessageHelper(mail, true);

    helper.setTo(para);

    helper.setSubject(asunto);

    // default = text/plain
    // true = text/html
    helper.setText(cuerpo, false);

    // hard coded a file path
    //FileSystemResource file = new FileSystemResource(new File("path/android.png"));

    //helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

    enviadorMails.send(mail);

  }
}
