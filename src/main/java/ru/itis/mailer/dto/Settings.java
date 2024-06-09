package ru.itis.mailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    private String vkToken;
    private String telegramToken;
    private String gmailUser;
    private String gmailPassword;
    private String maytapiApiUrl;
    private String maytapiToken;
    private String subscribeVk;
    private String subscribeWs;
    private String subscribeTg;
}
