package foro.hub.api.dto;

import foro.hub.api.modelo.Curso;
import foro.hub.api.modelo.StatusTopico;
import foro.hub.api.modelo.Topico;
import foro.hub.api.modelo.Usuario;

import java.time.LocalDateTime;

public record DetalleTopico(Long id,
                            String titulo,
                            String mensaje,
                            LocalDateTime fechaCreacion,
                            StatusTopico status,
                            DatosAutor autor,
                            DatosCurso curso
) {
    public DetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                new DatosAutor(topico.getAutor()),
                new DatosCurso(topico.getCurso())
        );
    }

    public record DatosAutor(Long id, String nombre, String email) {
        public DatosAutor(Usuario autor) {
            this(autor.getId(), autor.getNombre(), autor.getPassword());
        }
    }

    public record DatosCurso(Long id, String nombre, String categoria) {
        public DatosCurso(Curso curso) {
            this(curso.getId(), curso.getNombre(), curso.getCategoria());
        }
    }

}
