package com.aluracursos.challengeLiterAlura.model;

public enum Idioma {

    EN("Inglés"),
    ES("Español"),
    FR("Francés"),
    DE("Alemán"),
    IT("Italiano"),
    PT("Portugués"),
    OTRO("Otro"); // Valor por defecto

    private final String descripcion;

    Idioma(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Método que maneja valores en minúsculas como "en", "es", etc.
    public static Idioma fromString(String valor) {
        if (valor == null) {
            return OTRO; // Valor por defecto
        }

        // Convertir a mayúsculas y verificar el valor
        switch (valor.toLowerCase()) {
            case "en":
                return EN;
            case "es":
                return ES;
            case "fr":
                return FR;
            case "de":
                return DE;
            case "it":
                return IT;
            case "pt":
                return PT;
            default:
                return OTRO; // Valor por defecto en caso de no encontrar coincidencias
        }
    }
}
