package com.example.olympiad.web.dto.contest.JudgeTable;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class JudgeTableResponse {

    private Long id;

    private Long session;

    private String userName;

    private Long taskNumber;

    //private String fileContent;

    private Integer points;

    private String comment;

    private ZonedDateTime sentTime;

    private String fileName;

    private String fileExtension;

    private String state;

}
