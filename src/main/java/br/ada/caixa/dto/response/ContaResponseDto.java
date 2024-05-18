package br.ada.caixa.dto.response;

import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContaResponseDto {

    private Integer numero;
    private BigDecimal saldo;
    private Status status;
    private String tipo;
}
