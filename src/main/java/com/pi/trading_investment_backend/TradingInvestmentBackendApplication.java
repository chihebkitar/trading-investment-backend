package com.pi.trading_investment_backend;

import com.pi.trading_investment_backend.domain.User;
import com.pi.trading_investment_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class TradingInvestmentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingInvestmentBackendApplication.class, args);
	}
	@Bean
	public CommandLineRunner demoData(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				User user = new User();
				user.setName("Demo User"); //
				userRepository.save(user);

				System.out.println("Demo users created.");
			}
		};
	}
}
