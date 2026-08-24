package com.spring.security.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.security.app.dto.ProductoDTO;
import com.spring.security.app.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    /* ini inyeccion por constructor */
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    /* fin inyeccion por constructor */

    @GetMapping("/privado")
    public String privado() {
        return "Acceso permitido";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Bienvenido administrador";
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {

        return ResponseEntity.ok(
                productoService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> guardar(
            @RequestBody ProductoDTO productoDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.guardar(productoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDTO productoDTO) {

        return ResponseEntity.ok(
                productoService.actualizar(id, productoDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
