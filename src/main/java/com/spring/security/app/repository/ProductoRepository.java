package com.spring.security.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.app.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
