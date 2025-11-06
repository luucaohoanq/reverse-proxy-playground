package com.fpt.mss.msbrand_se181513.domain.brand;

import com.fpt.mss.dto.BrandResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> BrandResponse.builder()
                        .id(brand.getId())
                        .name(brand.getName())
                        .countryOfOrigin(brand.getCountryOfOrigin())
                        .build())
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .countryOfOrigin(brand.getCountryOfOrigin())
                .build();
    }
}
