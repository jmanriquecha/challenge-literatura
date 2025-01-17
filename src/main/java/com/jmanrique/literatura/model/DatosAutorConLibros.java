package com.jmanrique.literatura.model;

import java.util.List;

public record DatosAutorConLibros(
        String titulo,
        String autor,
        List<String> idiomas,
        String numeroDeDesargas
) {
}
