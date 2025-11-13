package com.biosalud.repository;
import com.biosalud.domain.laboratorio.TomaMuestra;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TomaMuestraRepository extends JpaRepository<TomaMuestra, Integer> {}