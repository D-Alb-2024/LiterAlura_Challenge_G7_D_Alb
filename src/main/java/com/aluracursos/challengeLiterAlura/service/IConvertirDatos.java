package com.aluracursos.challengeLiterAlura.service;

public interface IConvertirDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
