package com.example.gestaobancaria.conta.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroConta(Long numeroConta);

    boolean existsByNumeroConta(Long numeroConta);
}
