package com.fpt.mss.msbrand_se181513;

import com.fpt.mss.msbrand_se181513.domain.brand.Brand;
import com.fpt.mss.msbrand_se181513.domain.brand.BrandRepository;
import com.fpt.mss.msbrand_se181513.domain.product.BlindBox;
import com.fpt.mss.msbrand_se181513.domain.product.ProductRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = {"com.fpt.mss", "com.fpt.mss.msbrand_se181513"})
@RequiredArgsConstructor
@EnableFeignClients
public class MsBrandSe181513Application {

    public static void main(String[] args) {
        SpringApplication.run(MsBrandSe181513Application.class, args);
    }

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {

        if (brandRepository.count() == 0) {

            Brand brand1 = new Brand();
            brand1.setName("POP MART");
            brand1.setCountryOfOrigin("China");
            brandRepository.save(brand1);

            Brand brand2 = new Brand();
            brand2.setName("Funko");
            brand2.setCountryOfOrigin("USA");
            brandRepository.save(brand2);

            Brand brand3 = new Brand();
            brand3.setName("Kidrobot");
            brand3.setCountryOfOrigin("USA");
            brandRepository.save(brand3);
        }

        if (productRepository.count() == 0) {
            var pro1 =
                    BlindBox.builder()
                            .name("Mystic Dragon Figurine")
                            .brand(brandRepository.findById(1).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(150)
                            .price(29.99)
                            .rarity("Rare")
                            .build();

            var pro2 =
                    BlindBox.builder()
                            .name("Galactic Warrior Statue")
                            .brand(brandRepository.findById(2).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(80)
                            .price(49.99)
                            .rarity("Epic")
                            .build();

            var pro3 =
                    BlindBox.builder()
                            .name("Anime Hero Keychain")
                            .brand(brandRepository.findById(3).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(200)
                            .price(14.99)
                            .rarity("Common")
                            .build();

            productRepository.saveAll(List.of(pro1, pro2, pro3));
        }
    }
}
