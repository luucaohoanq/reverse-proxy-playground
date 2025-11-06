package com.fpt.mss.msblindbox_se181513.domain.blindbox;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlindBoxRequest {

    @NotBlank(message = "Name is required") private String name;

    @NotBlank(message = "Rarity is required") private String rarity;

    @NotNull(message = "Price is required") private Double price;

    @NotNull(message = "Stock is required") @Min(value = 1, message = "Stock is equal or greater than 0") @Max(value = 100, message = "Stock is less or greater than 100") private Integer stock;

    @NotNull(message = "Category ID is required") private Integer categoryId;

    @NotNull(message = "Brand ID is required") private Integer brandId;
}
