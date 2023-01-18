package com.group.libraryapp.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JavaUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByName(String name);

}
