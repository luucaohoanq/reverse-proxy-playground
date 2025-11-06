package com.fpt.mss.msblindbox_se181513.domain.blindbox;

import com.fpt.mss.api.ApiResponse;
import com.fpt.mss.dto.BlindBoxResponse;
import com.fpt.mss.msblindbox_se181513.client.BrandClient;
import com.fpt.mss.msblindbox_se181513.domain.category.Category;
import com.fpt.mss.msblindbox_se181513.domain.category.CategoryRepository;
import com.fpt.mss.msblindbox_se181513.feign.BlindBoxFeign;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlindBoxServiceImpl implements BlindBoxService {

    private final ProductRepository blindBoxRepository;
    private final CategoryRepository categoryRepository;
    private final BlindBoxFeign blindBoxFeign;
    private final BrandClient brandClient;

    @Override
    public List<BlindBoxResponse> getAll() {
        List<BlindBox> blindBoxes = blindBoxRepository.findAll();
        
        // Fetch brand names from Brand service
        try {
            ApiResponse<?> brandsResponse = brandClient.getAllBrands();
            List<?> brands = (List<?>) brandsResponse.getData();
            
            return blindBoxes.stream().map(blindBox -> {
                String brandName = null;
                // Find the brand name for this blindBox
                if (blindBox.getBrandId() != null && brands != null) {
                    for (Object brandObj : brands) {
                        if (brandObj instanceof java.util.LinkedHashMap) {
                            @SuppressWarnings("unchecked")
                            java.util.Map<String, Object> brandMap = (java.util.Map<String, Object>) brandObj;
                            Integer brandId = (Integer) brandMap.get("id");
                            if (brandId != null && brandId.equals(blindBox.getBrandId())) {
                                brandName = (String) brandMap.get("name");
                                break;
                            }
                        }
                    }
                }
                return BlindBox.toResponse(blindBox, brandName);
            }).toList();
        } catch (Exception e) {
            log.error("[BlindBoxService] Error fetching brands: {}", e.getMessage());
            // Return without brand names if service is unavailable
            return blindBoxes.stream().map(BlindBox::toResponse).toList();
        }
    }

    @Override
    public BlindBoxResponse getById(Integer id) {
        return blindBoxRepository
                .findById(id)
                .map(BlindBox::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Blind box not found"));
    }

    @Override
    public BlindBoxResponse create(BlindBoxRequest request) {
        var cat =
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category ID is required"));

        var blindBox =
                BlindBox.builder()
                        .name(request.getName())
                        .rarity(request.getRarity())
                        .price(request.getPrice())
                        .releaseDate(Date.from(Instant.now()))
                        .stock(request.getStock())
                        .category(cat)
                        .brandId(request.getBrandId())
                        .build();

        var saved = blindBoxRepository.save(blindBox);

        // Sync with Brand service
        try {
            log.info("[BlindBoxService] Syncing create to Brand service for BlindBox ID: {}", saved.getId());
            brandClient.create(request);
        } catch (Exception e) {
            log.error("[BlindBoxService] Error while syncing create to Brand service: {}", e.getMessage());
        }

        return BlindBox.toResponse(saved);
    }

    @Override
    public BlindBoxResponse update(Integer id, BlindBoxRequest request) {
        var data =
                blindBoxRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Blind box not found"));
        var cat =
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category ID is required"));

        var blindBox =
                BlindBox.builder()
                        .id(data.getId())
                        .name(request.getName())
                        .rarity(request.getRarity())
                        .price(request.getPrice())
                        .releaseDate(data.getReleaseDate())
                        .stock(request.getStock())
                        .category(cat)
                        .brandId(request.getBrandId())
                        .build();

        var updatedData = blindBoxRepository.save(blindBox);
        
        // Sync with Brand service
        try {
            log.info("[BlindBoxService] Syncing update to Brand service for BlindBox ID: {}", id);
            brandClient.update(id, request);
        } catch (Exception e) {
            log.error("[BlindBoxService] Error while syncing update to Brand service: {}", e.getMessage());
        }
        
        return BlindBox.toResponse(updatedData);
    }

    @Override
    public void delete(Integer id) {
        var data =
                blindBoxRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Blind box not found"));
        blindBoxRepository.delete(data);
        
        // Sync with Brand service
        try {
            log.info("[BlindBoxService] Syncing delete to Brand service for BlindBox ID: {}", id);
            brandClient.delete(id);
        } catch (Exception e) {
            log.error("[BlindBoxService] Error while syncing delete to Brand service: {}", e.getMessage());
        }
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    
    @Override
    public List<?> getAllBrands() {
        try {
            log.info("[BlindBoxService] Fetching all brands from Brand service");
            ApiResponse<?> response = brandClient.getAllBrands();
            return (List<?>) response.getData();
        } catch (Exception e) {
            log.error("[BlindBoxService] Error while fetching brands from Brand service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch brands: " + e.getMessage());
        }
    }
}
