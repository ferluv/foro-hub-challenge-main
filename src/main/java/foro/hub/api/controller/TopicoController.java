package foro.hub.api.controller;

import foro.hub.api.dto.DetalleTopico;
import foro.hub.api.dto.TopicoRequest;
import foro.hub.api.dto.TopicoResponse;
import foro.hub.api.dto.TopicoUpdateRequest;
import foro.hub.api.modelo.Curso;
import foro.hub.api.modelo.Topico;
import foro.hub.api.modelo.Usuario;
import foro.hub.api.repository.CursoRepository;
import foro.hub.api.repository.TopicoRepository;
import foro.hub.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    //Registrar topico
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoResponse> registrar(@RequestBody @Valid TopicoRequest datos,
                                                    UriComponentsBuilder uriBuilder) {
        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un tópico con ese título y mensaje");
        }

        Usuario autor = usuarioRepository.getReferenceById(datos.autorId());
        Curso curso = cursoRepository.getReferenceById(datos.cursoId());

        Topico topico = new Topico(datos, autor, curso);
        repository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoResponse(topico));
    }


    //Listar topico
    @GetMapping
    public List<TopicoResponse> listarTopicos() {
        return repository.findAll().stream()
                .map(TopicoResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleTopico> detalle(@PathVariable Long id) {
        Topico topico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        return ResponseEntity.ok(new DetalleTopico(topico));
    }

    //Actualizar topico
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoResponse> actualizar(@RequestBody @Valid TopicoUpdateRequest datos) {
        if (repository.existsByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), datos.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro tópico con ese título y mensaje");
        }

        Topico topico = repository.findById(datos.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());

        return ResponseEntity.ok(new TopicoResponse(topico));
    }


    //Eliminar topico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Topico> topicoOptional = repository.findById(id);

        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }
}
