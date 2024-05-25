package ru.itis.mailer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolBoy {
    private String name;
    private String city;
    private String school;
    private String grade;
    private String score;
    private String subject;
    private String vk;
}
