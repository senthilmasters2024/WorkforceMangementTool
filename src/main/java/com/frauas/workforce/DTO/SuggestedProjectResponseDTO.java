package com.frauas.workforce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedProjectResponseDTO {

    private ApplicationResponseDTO application;
    private ProjectResponseDto project;
}
