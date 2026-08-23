package com.spring.security.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.security.app.dto.ProductoDTO;
import com.spring.security.app.entity.Producto;
import com.spring.security.app.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    /* ini inyeccion por constructor */
    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    /* fin inyeccion por constructor */

    @Override
    public List<ProductoDTO> listar() {

        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public ProductoDTO buscarPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        return convertirADTO(producto);
    }

    @Override
    public ProductoDTO guardar(ProductoDTO productoDTO) {

        Producto producto = convertirAEntity(productoDTO);

        Producto productoGuardado =
                productoRepository.save(producto);

        return convertirADTO(productoGuardado);
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());

        Producto productoActualizado =
                productoRepository.save(producto);

        return convertirADTO(productoActualizado);
    }

    @Override
    public void eliminar(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }

    private ProductoDTO convertirADTO(Producto producto) {

        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio()
        );
    }

    private Producto convertirAEntity(ProductoDTO dto) {

        return new Producto(
                dto.getId(),
                dto.getNombre(),
                dto.getPrecio()
        );
    }

}
