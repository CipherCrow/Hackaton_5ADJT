package br.com.hackaton.priorizasus.exception;

public class EnumInvalidoException extends RuntimeException {
    public EnumInvalidoException(String mensagem) {
        super(mensagem);
    }
}