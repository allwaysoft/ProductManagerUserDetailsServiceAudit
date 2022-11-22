package com.example;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Integer> {

    List<LoginLog> findAllByOrderByIdDesc();

}
