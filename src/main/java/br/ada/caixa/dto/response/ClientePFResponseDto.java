package br.ada.caixa.dto.response;

import br.ada.caixa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientePFResponseDto {

    private Long id;
    private String cpf;
    private String nome;
    private LocalDate dataCadastro;
    private Status status;
}