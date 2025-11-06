package com.fpt.mss.msblindbox_se181513.feign;

import com.fpt.mss.msblindbox_se181513.configs.FeignConfig;
import com.fpt.mss.msblindbox_se181513.domain.blindbox.BlindBoxRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "blindbox-service",
        url = "http://localhost:4005/api/blindboxes",
        configuration = FeignConfig.class)
public interface BlindBoxFeign {

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody BlindBoxRequest request);

    @PutMapping("/{id}")
    ResponseEntity<?> update(@Valid @RequestBody BlindBoxRequest request, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id);
}
