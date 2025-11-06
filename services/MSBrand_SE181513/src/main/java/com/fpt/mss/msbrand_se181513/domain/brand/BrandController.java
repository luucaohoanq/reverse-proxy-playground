package com.fpt.mss.msbrand_se181513.domain.brand;

import com.fpt.mss.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(brandService.getAllBrands()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(brandService.getBrandById(id)));
    }
}
