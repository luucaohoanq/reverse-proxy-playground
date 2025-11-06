package com.fpt.mss.msblindbox_se181513;

import com.fpt.mss.msblindbox_se181513.domain.blindbox.BlindBox;
import com.fpt.mss.msblindbox_se181513.domain.blindbox.ProductRepository;
import com.fpt.mss.msblindbox_se181513.domain.category.Category;
import com.fpt.mss.msblindbox_se181513.domain.category.CategoryRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = {"com.fpt.mss", "com.fpt.mss.msblindbox_se181513"})
@RequiredArgsConstructor
@EnableFeignClients
public class MsBlindBoxSe181513Application {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(MsBlindBoxSe181513Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {

        if (categoryRepository.count() == 0) {

            Category category1 = new Category();
            category1.setName("Electronics");
            category1.setDescription("Mystical creatures, wizards, and legendary beings.");
            category1.setRarityLevel("Common to Ultra Rare");
            category1.setPriceRange("$10 - $60");
            categoryRepository.save(category1);

            Category category2 = new Category();
            category2.setName("Gaming");
            category2.setDescription("Blind boxes featuring characters from popular video games.");
            category2.setRarityLevel("Common to Epic");
            category2.setPriceRange("$25 - $70");
            categoryRepository.save(category2);

            Category category3 = new Category();
            category3.setName("Sci-Fi");
            category3.setDescription("Space, futuristic themes, and robotic collectibles.");
            category3.setRarityLevel("Rare to Legendary");
            category3.setPriceRange("$30 - $80");
            categoryRepository.save(category3);

            Category category4 = new Category();
            category4.setName("Anime");
            category4.setDescription("Popular anime characters and themed mystery figures.");
            category4.setRarityLevel("Common to Legendary");
            category4.setPriceRange("$15 - $100");

            Category category5 = new Category();
            category5.setName("Steampunk");
            category5.setDescription("Victorian-era inspired mechanical and fantasy figures.");
            category5.setRarityLevel("Rare to Epic");
            category5.setPriceRange("$100 - $190");
            categoryRepository.save(category5);
        }

        if (productRepository.count() == 0) {
            var pro1 =
                    BlindBox.builder()
                            .name("Mystic Dragon Figurine")
                            .category(categoryRepository.findById(1).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(150)
                            .price(29.99)
                            .rarity("Rare")
                            .build();

            var pro2 =
                    BlindBox.builder()
                            .name("Galactic Warrior Statue")
                            .category(categoryRepository.findById(3).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(80)
                            .price(49.99)
                            .rarity("Epic")
                            .build();

            var pro3 =
                    BlindBox.builder()
                            .name("Anime Hero Keychain")
                            .category(categoryRepository.findById(4).orElse(null))
                            .releaseDate(Date.from(Instant.now()))
                            .stock(200)
                            .price(14.99)
                            .rarity("Common")
                            .build();

            productRepository.saveAll(List.of(pro1, pro2, pro3));
        }
    }
}
