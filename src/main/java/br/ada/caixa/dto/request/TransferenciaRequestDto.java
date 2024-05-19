package br.ada.caixa.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferenciaRequestDto {

    private Long numeroContaOrigem;
    private Long numeroContaDestino;
    private BigDecimal valor;
}
