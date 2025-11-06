package com.fpt.mss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandResponse {
    private Integer id;
    private String name;
    private String countryOfOrigin;
}
