package br.ada.caixa.controller;

import br.ada.caixa.dto.request.ContaRequestDto;
import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.service.ContaCorrenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas/corrente")
public class ContaCorrenteController {
    
    final private ContaCorrenteService contaCorrenteService;
    
    public ContaCorrenteController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping
    public ResponseEntity<ContaResponseDto> inserir(@RequestBody ContaRequestDto contaRequestDto) {
        ContaResponseDto contaResponseDto = contaCorrenteService.inserir(contaRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaResponseDto);
    }
}
