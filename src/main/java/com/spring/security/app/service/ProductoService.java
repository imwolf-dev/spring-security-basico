package com.spring.security.app.service;

import java.util.List;

import com.spring.security.app.dto.ProductoDTO;

public interface ProductoService {

    List<ProductoDTO> listar();

    ProductoDTO buscarPorId(Long id);

    ProductoDTO guardar(ProductoDTO productoDTO);

    ProductoDTO actualizar(Long id, ProductoDTO productoDTO);

    void eliminar(Long id);

}
