package br.ada.caixa.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaldoResponseDto {

    private String numeroConta;
    private BigDecimal saldo;

}
