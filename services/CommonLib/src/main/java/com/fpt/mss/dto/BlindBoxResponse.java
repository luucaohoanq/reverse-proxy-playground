package com.fpt.mss.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlindBoxResponse {

    private Integer id;
    private String name;
    private String rarity;
    private Double price;
    private Date releaseDate;
    private Integer stock;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
}
