package br.ada.caixa.dto.request.clientePJ;

import br.ada.caixa.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizacaoPJRequestDto {

    private Status status;
}
