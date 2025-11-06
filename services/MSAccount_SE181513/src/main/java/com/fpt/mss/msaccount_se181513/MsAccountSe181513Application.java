package com.fpt.mss.msaccount_se181513;

import com.fpt.mss.enums.Role;
import com.fpt.mss.msaccount_se181513.domain.user.Account;
import com.fpt.mss.msaccount_se181513.domain.user.AccountRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.fpt.mss", "com.fpt.mss.msaccount_se181513"})
@RequiredArgsConstructor
public class MsAccountSe181513Application {

    public static void main(String[] args) {
        SpringApplication.run(MsAccountSe181513Application.class, args);
    }

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword = "@2";
    private String hashedPassword = "";

    @PostConstruct
    public void hashDefaultPassword() {
        hashedPassword = passwordEncoder.encode(defaultPassword);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {

        if (accountRepository.count() == 0) {
            var account1 =
                    Account.builder()
                            .username("adminsys")
                            .email("admin@blindboxes.vn")
                            .password(hashedPassword)
                            .role(Role.ADMIN)
                            .isActive(true)
                            .build();

            var account2 =
                    Account.builder()
                            .username("johndoe")
                            .email("john@blindboxes.vn")
                            .password(hashedPassword)
                            .role(Role.MEMBER)
                            .isActive(true)
                            .build();

            var account3 =
                    Account.builder()
                            .username("modmichel")
                            .email("michel@blindboxes.vn")
                            .password(hashedPassword)
                            .role(Role.MODERATOR)
                            .isActive(true)
                            .build();

            var account4 =
                    Account.builder()
                            .username("vanvan")
                            .email("vanavan@blindboxes.vn")
                            .password(defaultPassword)
                            .role(Role.MEMBER)
                            .isActive(false)
                            .build();

            var account5 =
                    Account.builder()
                            .username("devops")
                            .email("dev@blindboxes.vn")
                            .password(defaultPassword)
                            .role(Role.DEVELOPER)
                            .isActive(true)
                            .build();

            accountRepository.saveAll(
                    List.of(account1, account2, account3, account4, account5));
        }
        else {
            System.out.println("Accounts already initialized. Skipping seeding.");
        }
    }
}
