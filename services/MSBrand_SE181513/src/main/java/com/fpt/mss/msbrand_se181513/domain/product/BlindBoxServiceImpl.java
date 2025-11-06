package com.fpt.mss.msbrand_se181513.domain.product;

import com.fpt.mss.dto.BlindBoxResponse;
import com.fpt.mss.msbrand_se181513.domain.brand.BrandRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlindBoxServiceImpl implements BlindBoxService {

    private final ProductRepository blindBoxRepository;
    private final BrandRepository brandRepository;

    @Override
    public List<BlindBoxResponse> getAll() {
        return blindBoxRepository.findAll().stream().map(BlindBox::toResponse).toList();
    }

    @Override
    public BlindBoxResponse create(BlindBoxRequest request) {
        var brand =
                brandRepository
                        .findById(request.getBrandId())
                        .orElseThrow(() -> new IllegalArgumentException("Brand ID is required"));

        var blindBox =
                BlindBox.builder()
                        .name(request.getName())
                        .rarity(request.getRarity())
                        .price(request.getPrice())
                        .releaseDate(Date.from(Instant.now()))
                        .stock(request.getStock())
                        .brand(brand)
                        .build();
        var data = blindBoxRepository.save(blindBox);
        return BlindBox.toResponse(data);
    }

    @Override
    public BlindBoxResponse update(Integer id, BlindBoxRequest request) {
        var data =
                blindBoxRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Blind box not found"));
        var brand =
                brandRepository
                        .findById(request.getBrandId())
                        .orElseThrow(() -> new IllegalArgumentException("Brand ID is required"));

        var blindBox =
                BlindBox.builder()
                        .id(data.getId())
                        .name(request.getName())
                        .rarity(request.getRarity())
                        .price(request.getPrice())
                        .releaseDate(data.getReleaseDate())
                        .stock(request.getStock())
                        .brand(brand)
                        .build();

        var updatedData = blindBoxRepository.save(blindBox);
        return BlindBox.toResponse(updatedData);
    }

    @Override
    public void delete(Integer id) {
        var data =
                blindBoxRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Blind box not found"));
        blindBoxRepository.delete(data);
    }
}
