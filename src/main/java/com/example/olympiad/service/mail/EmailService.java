package com.example.olympiad.service.mail;


import com.example.olympiad.domain.mail.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    void sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    void sendMailWithAttachment(EmailDetails details);
}
