package com.example.olympiad.domain.mail;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String toAddress;
    private String subject;
    private String body;
    private String attachment;
}
