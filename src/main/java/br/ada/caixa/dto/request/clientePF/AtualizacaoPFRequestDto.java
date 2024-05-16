package br.ada.caixa.dto.request.clientePF;

import br.ada.caixa.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizacaoPFRequestDto {

    private Status status;
}
