package br.ada.caixa.dto.request.clientePJ;

import br.ada.caixa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InsercaoPJRequestDto {

    private String cnpj;
    private String nomeFantasia;
    private String razaoSocial;
    private LocalDate dataCadastro;
    private Status status;
}
