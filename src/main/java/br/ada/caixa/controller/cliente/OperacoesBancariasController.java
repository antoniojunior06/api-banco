package br.ada.caixa.controller.cliente;

import br.ada.caixa.dto.request.DepositoRequestDto;
import br.ada.caixa.dto.request.SaqueRequestDto;
import br.ada.caixa.dto.request.TransferenciaRequestDto;
import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operacoes")
public class OperacoesBancariasController {

    private final ContaService contaService;

    public OperacoesBancariasController(ContaService contaService) {
        this.contaService = contaService;
    }


    @PostMapping("/deposito")
    public ResponseEntity<ContaResponseDto> depositar(@RequestBody DepositoRequestDto depositoRequestDto) {
        ContaResponseDto contaResponseDto = contaService.depositar(depositoRequestDto);
        return ResponseEntity.ok(contaResponseDto);
    }

    @PostMapping("/saque")
    public ResponseEntity<ContaResponseDto> sacar(@RequestBody SaqueRequestDto saqueRequestDto) {
        ContaResponseDto contaResponseDto = contaService.sacar(saqueRequestDto);
        return ResponseEntity.ok(contaResponseDto);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<ContaResponseDto> transferir(@RequestBody TransferenciaRequestDto transfereRequestDto) {
        ContaResponseDto contaResponseDto = contaService.transferir(transfereRequestDto);
        return ResponseEntity.ok(contaResponseDto);
    }

    @GetMapping("/saldo/{numeroConta}")
    public ResponseEntity<ContaResponseDto> consultarSaldo(@PathVariable Long numeroConta) {
        ContaResponseDto contaResponseDto = contaService.consultarSaldo(numeroConta);
        return ResponseEntity.ok(contaResponseDto);
    }

    @PostMapping("/investimento")
    public void investir() {

    }

}
