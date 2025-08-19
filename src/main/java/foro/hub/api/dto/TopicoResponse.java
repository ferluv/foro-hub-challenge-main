package foro.hub.api.dto;

public record TopicoResponse(Long id,
                             String titulo,
                             String mensaje,
                             String autor,
                             String curso
) {
    public TopicoResponse(foro.hub.api.modelo.Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );

    }
}
