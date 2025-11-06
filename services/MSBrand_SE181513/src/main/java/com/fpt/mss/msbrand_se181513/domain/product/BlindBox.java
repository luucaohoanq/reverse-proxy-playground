package com.fpt.mss.msbrand_se181513.domain.product;

import com.fpt.mss.dto.BlindBoxResponse;
import com.fpt.mss.msbrand_se181513.domain.brand.Brand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blind_boxes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlindBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(length = 50)
    private String rarity;

    private Double price;

    private Date releaseDate;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    private Integer categoryId;

    public static BlindBoxResponse toResponse(BlindBox blindBox) {
        return BlindBoxResponse.builder()
                .id(blindBox.getId())
                .name(blindBox.getName())
                .rarity(blindBox.getRarity())
                .price(blindBox.getPrice())
                .releaseDate(blindBox.getReleaseDate())
                .stock(blindBox.getStock())
                .brandName(blindBox.getBrand().getName())
                .build();
    }
}
