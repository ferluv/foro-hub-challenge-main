package foro.hub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoUpdateRequest(@NotNull Long id,
                                  @NotBlank String titulo,
                                  @NotBlank String mensaje

) {
}
