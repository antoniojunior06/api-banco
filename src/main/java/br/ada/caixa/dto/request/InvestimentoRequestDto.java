package br.ada.caixa.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestimentoRequestDto {

    private Long numeroConta;
    private BigDecimal valor;
}
