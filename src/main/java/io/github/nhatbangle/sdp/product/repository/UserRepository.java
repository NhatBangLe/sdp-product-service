package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}