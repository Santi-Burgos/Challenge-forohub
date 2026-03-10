package com.forohubBurgos.forohub.controller;

import com.forohubBurgos.forohub.domain.topico.DatosListaTopico;
import com.forohubBurgos.forohub.domain.topico.DatosRegistroTopico;
import com.forohubBurgos.forohub.domain.topico.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/topicos")
//@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        var nuevoTopico = topicoService.registrar(datos);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(nuevoTopico.id()).toUri();

        return ResponseEntity.created(uri).body(nuevoTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(@PageableDefault(size = 10, sort = {"fechaCreacion"}, direction =Sort.Direction.ASC) Pageable paginacion) {
        var pagina = topicoService.listar(paginacion);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping({"/curso/{nombreCurso}", "/anio/{anioCreacion}", "/curso/{nombreCurso}/anio/{anioCreacion}", "/anio/{anioCreacion}/curso/{nombreCurso}"})
    public ResponseEntity<Page<DatosListaTopico>> buscar(@PathVariable Optional<String> nombreCurso,
                                                         @PathVariable Optional<Integer> anioCreacion,
                                                         @PageableDefault(size = 10, sort = {"fechaCreacion"}, direction = Sort.Direction.ASC) Pageable paginacion) {
        var pagina = topicoService.buscar(nombreCurso, anioCreacion, paginacion);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping({"/{id}", "/"})
    public ResponseEntity obtener(@PathVariable Long id) {
        var topico = topicoService.obtener(id);
        return ResponseEntity.ok(new DatosListaTopico(topico));
    }

    @Transactional
    @PutMapping({"/{id}", "/"})
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosRegistroTopico datos) {
        var topico = topicoService.actualizar(id, datos);
        return ResponseEntity.ok(topico);
    }

    @Transactional
    @DeleteMapping({"/{id}", "/"})
    public ResponseEntity eliminar(@PathVariable Long id) {
        topicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
