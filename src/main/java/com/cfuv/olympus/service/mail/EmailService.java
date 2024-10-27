package com.cfuv.olympus.service.mail;


import com.cfuv.olympus.domain.mail.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    void sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    void sendMailWithAttachment(EmailDetails details);
}
