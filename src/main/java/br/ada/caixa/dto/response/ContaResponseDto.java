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

    private Long id;
    private Integer numero;
    private BigDecimal saldo;
    private LocalDate dataCriacao;
    private Cliente cliente;
    private Status status;
}