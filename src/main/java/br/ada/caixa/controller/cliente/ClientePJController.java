package br.ada.caixa.controller.cliente;


import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.filter.ClientePJFilter;
import br.ada.caixa.dto.request.clientePJ.AtualizacaoPJRequestDto;
import br.ada.caixa.dto.request.clientePJ.InsercaoPJRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.dto.response.ClientePJResponseDto;
import br.ada.caixa.service.ClientePJService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/pj")
public class ClientePJController {

    final private ClientePJService clientePJService;

    public ClientePJController(ClientePJService clientePFService) {
        this.clientePJService = clientePFService;
    }

    @PostMapping
    public ResponseEntity<ClientePJResponseDto> inserir(@RequestBody InsercaoPJRequestDto clienteDto) {
        ClientePJResponseDto clientePJResponseDto = clientePJService.inserir(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientePJResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientePJResponseDto> atualizar(@PathVariable Long id,
                                                          @RequestBody AtualizacaoPJRequestDto atualizacaoPJRequestDto) {
        ClientePJResponseDto clientePJResponseDto = clientePJService.atualizar(id, atualizacaoPJRequestDto);
        return ResponseEntity.ok(clientePJResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        clientePJService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ClientePJResponseDto>> listarTodos() {
        List<ClientePJResponseDto> listaClientes = clientePJService.listarTodos();
        return ResponseEntity.ok(listaClientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientePJResponseDto> buscarPorId(@PathVariable Long id) {
        ClientePJResponseDto clientePJResponseDto = clientePJService.buscarPorId(id);
        return ResponseEntity.ok(clientePJResponseDto);
    }

    @GetMapping("/razao")
    public ResponseEntity<List<ClientePJResponseDto>> buscarPorRazaoSocial(ClientePJFilter filter) {
        List<ClientePJResponseDto> clientePJResponseDto = clientePJService.buscarPorRazaoSocial(filter);
        return ResponseEntity.ok(clientePJResponseDto);
    }

    @GetMapping("/cnpj")
    public ResponseEntity<ClientePJResponseDto> buscarPorCnpj(ClientePJFilter filter) {
        ClientePJResponseDto clientePJResponseDto = clientePJService.buscarPorCnpj(filter);
        return ResponseEntity.ok(clientePJResponseDto);
    }



}
