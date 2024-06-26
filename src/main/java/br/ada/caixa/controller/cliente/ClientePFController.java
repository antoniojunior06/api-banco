package br.ada.caixa.controller.cliente;


import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.request.clientePF.AtualizacaoPFRequestDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFContaDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.service.ClientePFService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/pf")
public class ClientePFController {

    final private ClientePFService clientePFService;

    public ClientePFController(ClientePFService clientePFService) {
        this.clientePFService = clientePFService;
    }

    @PostMapping
    public ResponseEntity<ClientePFResponseDto> inserir(@RequestBody InsercaoPFRequestDto clienteDto) {
        ClientePFResponseDto clientePFResponseDto = clientePFService.inserir(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientePFResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientePFResponseDto> atualizar(@PathVariable String id,
                                                          @RequestBody AtualizacaoPFRequestDto atualizacaoPFRequestDto) {
        ClientePFResponseDto clientePFResponseDto = clientePFService.atualizar(id, atualizacaoPFRequestDto);
        return ResponseEntity.ok(clientePFResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        clientePFService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ClientePFResponseDto>> listarTodos() {
        List<ClientePFResponseDto> listaClientes = clientePFService.listarTodos();
        return ResponseEntity.ok(listaClientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientePFResponseDto> buscarPorId(@PathVariable String id) {
        ClientePFResponseDto clientePFResponseDto = clientePFService.buscarPorId(id);
        return ResponseEntity.ok(clientePFResponseDto);
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ClientePFResponseDto>> buscarPorNome(ClientePFFilter filter) {
        List<ClientePFResponseDto> clientePFResponseDto = clientePFService.buscarPorNome(filter);
        return ResponseEntity.ok(clientePFResponseDto);
    }

    @PostMapping("/cp")
    public ResponseEntity<ClientePFResponseDto> criarContaPoupanca(@RequestBody InsercaoPFContaDto insercaoPFContaDto) {
        ClientePFResponseDto clientePFResponseDto = clientePFService.adicionarContaPoupanca(insercaoPFContaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientePFResponseDto);
    }

    @PostMapping("/ci")
    public ResponseEntity<ClientePFResponseDto> criarContaInvestimento(@RequestBody InsercaoPFContaDto insercaoPFContaDto) {
        ClientePFResponseDto clientePFResponseDto = clientePFService.adicionarContaInvestimento(insercaoPFContaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientePFResponseDto);
    }


}
