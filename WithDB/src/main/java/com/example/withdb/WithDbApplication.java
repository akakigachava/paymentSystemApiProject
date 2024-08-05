package com.example.withdb;

import com.example.withdb.dao.BaseProductDAO;
import com.example.withdb.entity.ERole;
import com.example.withdb.entity.Role;
import com.example.withdb.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WithDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithDbApplication.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository repository, RoleRepository roleRepository){
		return args -> {
			createRoleTable(roleRepository);
		};
	}


	public void createRoleTable(RoleRepository roleRepository){

		Role user = new Role();
		user.setId(1);
		user.setName(ERole.ROLE_USER);


		Role admin = new Role();
		admin.setId(2);
		admin.setName(ERole.ROLE_ADMIN);

		roleRepository.save(user);
		roleRepository.save(admin);

	}
}
