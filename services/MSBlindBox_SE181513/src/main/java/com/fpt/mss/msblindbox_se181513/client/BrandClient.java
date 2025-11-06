package com.fpt.mss.msblindbox_se181513.client;

import com.fpt.mss.api.ApiResponse;
import com.fpt.mss.msblindbox_se181513.configs.FeignConfig;
import com.fpt.mss.msblindbox_se181513.domain.blindbox.BlindBoxRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msbrand", url = "${services.gateway.url}", configuration = FeignConfig.class)
public interface BrandClient {
    
    // BlindBox operations on Brand service
    @PostMapping("/api/blindboxes")
    ApiResponse<?> create(@RequestBody BlindBoxRequest request);
    
    @PutMapping("/api/blindboxes/{id}")
    ApiResponse<?> update(@PathVariable("id") Integer id, @RequestBody BlindBoxRequest request);
    
    @DeleteMapping("/api/blindboxes/{id}")
    ApiResponse<?> delete(@PathVariable("id") Integer id);
    
    // Brand operations
    @GetMapping("/api/brands")
    ApiResponse<?> getAllBrands();
    
    @GetMapping("/api/brands/{id}")
    ApiResponse<?> getBrandById(@PathVariable("id") Integer id);
}
