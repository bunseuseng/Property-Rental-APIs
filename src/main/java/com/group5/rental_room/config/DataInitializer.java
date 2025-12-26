//package com.group5.rental_room.config;
//
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.group5.rental_room.entity.UserEntity;
//import com.group5.rental_room.repositpory.UserRepository;
//
//
//@Configuration
//public class DataInitializer {
//	@Bean
//    CommandLineRunner initAdmin(UserRepository userRepository,
//                                PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (!userRepository.existsByUsername("admin")) {
//                UserEntity admin = new UserEntity();
//                admin.setFullName("System Admin");
//                admin.setUsername("admin");
//                admin.setEmail("admin@rentroom.com");
//                admin.setPhone("000000000");
//                admin.setGender("OTHER");
//                admin.setStatus("ACTIVE");
//                admin.setPassword(passwordEncoder.encode("Admin@123"));
//                // admin.setRole("ADMIN"); (later)
//
//                userRepository.save(admin);
//            }
//        };
//    }
//}
