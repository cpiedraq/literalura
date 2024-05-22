package com.alura.literalura.service;

public interface IConvertData
{
    <T> T getDataConverted(String json, Class<T> clase);
}
