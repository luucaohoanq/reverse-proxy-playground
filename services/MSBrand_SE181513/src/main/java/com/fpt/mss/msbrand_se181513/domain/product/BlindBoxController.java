package com.fpt.mss.msbrand_se181513.domain.product;

import com.fpt.mss.annotations.RequireRole;
import com.fpt.mss.api.ApiResponse;
import com.fpt.mss.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blindboxes")
@RequiredArgsConstructor
public class BlindBoxController {

    private final BlindBoxService blindBoxService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(blindBoxService.getAll()));
    }

    @PostMapping
    @RequireRole(Role.ADMIN)
    public ResponseEntity<?> create(@Valid @RequestBody BlindBoxRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(blindBoxService.create(request)));
    }

    @PutMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public ResponseEntity<?> update(
            @Valid @RequestBody BlindBoxRequest request, @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(blindBoxService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @RequireRole(Role.ADMIN)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        blindBoxService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
