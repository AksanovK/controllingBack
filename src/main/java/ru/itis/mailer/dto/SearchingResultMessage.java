package ru.itis.mailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.models.SchoolBoy;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchingResultMessage {
    private String status;
    private List<SchoolBoy> result;
}
