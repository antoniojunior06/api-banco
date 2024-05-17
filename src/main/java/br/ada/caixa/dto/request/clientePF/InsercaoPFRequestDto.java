package br.ada.caixa.dto.request.clientePF;

import br.ada.caixa.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InsercaoPFRequestDto {

    private String cpf;
    private String nome;
}
