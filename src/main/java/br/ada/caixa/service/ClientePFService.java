package br.ada.caixa.service;

import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.request.clientePF.AtualizacaoPFRequestDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFContaDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.entity.conta.Conta;
import br.ada.caixa.entity.conta.ContaCorrente;
import br.ada.caixa.entity.conta.ContaInvestimento;
import br.ada.caixa.entity.conta.ContaPoupanca;
import br.ada.caixa.enums.Status;
import br.ada.caixa.exception.ValidacaoException;
import br.ada.caixa.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ClientePFService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    public ClientePFService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    public ClientePFResponseDto inserir(InsercaoPFRequestDto insercaoPFRequestDto) {
        ClientePF cliente = modelMapper.map(insercaoPFRequestDto, ClientePF.class);
        cliente.setStatus(Status.ATIVO);
        cliente.setDataCadastro(LocalDate.now());

        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(new Random().nextLong());
        contaCorrente.setDataCriacao(LocalDate.now());
        contaCorrente.setCliente(cliente);
        contaCorrente.setSaldo(BigDecimal.ZERO);
        contaCorrente.setStatus(Status.ATIVO);

        cliente.setContas(new ArrayList<>());
        cliente.getContas().add(contaCorrente);

        cliente = clienteRepository.save(cliente);

        return modelMapper.map(cliente, ClientePFResponseDto.class);
    }

    public ClientePFResponseDto atualizar(String id, AtualizacaoPFRequestDto atualizacaoPFRequestDto) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    modelMapper.map(atualizacaoPFRequestDto, cliente);
                    return clienteRepository.save(cliente);
                })
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));

    }

    public void excluir(String id) {
        clienteRepository.deleteById(id);
    }

    public List<ClientePFResponseDto> listarTodos() {
        return clienteRepository.findAllPF()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .collect(Collectors.toList());

    }

    public ClientePFResponseDto buscarPorId(String id) {
        return clienteRepository.findById(id)
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
    }

    public List<ClientePFResponseDto> buscarPorNome(ClientePFFilter filter) {
        return clienteRepository.findAllByName(filter.getNome())
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .toList();
    }

    public ClientePFResponseDto adicionarContaPoupanca(InsercaoPFContaDto insercaoPFContaDto) {
        var contaPoupanca = new ContaPoupanca();
        Cliente cliente = criarConta(insercaoPFContaDto, contaPoupanca);
        return modelMapper.map(cliente, ClientePFResponseDto.class);
    }

    public ClientePFResponseDto adicionarContaInvestimento(InsercaoPFContaDto insercaoPFContaDto) {
        var contaInvestimento = new ContaInvestimento();
        Cliente cliente = criarConta(insercaoPFContaDto, contaInvestimento);
        return modelMapper.map(cliente, ClientePFResponseDto.class);
    }


    private Cliente criarConta(InsercaoPFContaDto insercaoPFContaDto, Conta conta) {
        Cliente cliente = clienteRepository.findByDocumento(insercaoPFContaDto.getCpf()).orElseThrow();

        conta.setNumero(new Random().nextLong());
        conta.setDataCriacao(LocalDate.now());
        conta.setCliente(cliente);
        conta.setSaldo(BigDecimal.ZERO);
        conta.setStatus(Status.ATIVO);

        cliente.setContas(new ArrayList<>());
        cliente.getContas().add(conta);

        cliente = clienteRepository.save(cliente);

        return cliente;
    }

}
