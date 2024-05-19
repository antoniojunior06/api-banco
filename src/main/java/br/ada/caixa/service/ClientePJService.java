package br.ada.caixa.service;

import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.filter.ClientePJFilter;
import br.ada.caixa.dto.request.clientePF.InsercaoPFContaDto;
import br.ada.caixa.dto.request.clientePJ.AtualizacaoPJRequestDto;
import br.ada.caixa.dto.request.clientePJ.InsercaoPJContaDto;
import br.ada.caixa.dto.request.clientePJ.InsercaoPJRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.dto.response.ClientePJResponseDto;
import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePJ;
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
public class ClientePJService {

    final private ClienteRepository clienteRepository;
    final private ModelMapper modelMapper;

    public ClientePJService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    public ClientePJResponseDto inserir(InsercaoPJRequestDto insercaoPJRequestDto) {
        ClientePJ cliente = modelMapper.map(insercaoPJRequestDto, ClientePJ.class);
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
        return modelMapper.map(cliente, ClientePJResponseDto.class);
    }

    public ClientePJResponseDto atualizar(String id, AtualizacaoPJRequestDto atualizacaoPJRequestDto) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    modelMapper.map(atualizacaoPJRequestDto, cliente);
                    return clienteRepository.save(cliente);
                })
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));

    }

    public void excluir(String id) {
        clienteRepository.deleteById(id);
    }

    public List<ClientePJResponseDto> listarTodos() {
        return clienteRepository.findAllPJ()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .collect(Collectors.toList());

    }

    public ClientePJResponseDto buscarPorId(String id) {
        return clienteRepository.findById(id)
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
    }

    public List<ClientePJResponseDto> buscarPorRazaoSocial(ClientePJFilter filter) {
        return clienteRepository.findAllByRazaoSocial(filter.getRazaoSocial())
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .toList();
    }


    public ClientePJResponseDto adicionarContaPoupanca(InsercaoPJContaDto insercaoPJContaDto) {
        var contaPoupanca = new ContaPoupanca();
        Cliente cliente = criarConta(insercaoPJContaDto, contaPoupanca);
        return modelMapper.map(cliente, ClientePJResponseDto.class);
    }

    public ClientePJResponseDto adicionarContaInvestimento(InsercaoPJContaDto insercaoPJContaDto) {
        var contaInvestimento = new ContaInvestimento();
        Cliente cliente = criarConta(insercaoPJContaDto, contaInvestimento);
        return modelMapper.map(cliente, ClientePJResponseDto.class);
    }

    private Cliente criarConta(InsercaoPJContaDto insercaoPJContaDto, Conta conta) {
        Cliente cliente = clienteRepository.findByDocumento(insercaoPJContaDto.getCnpj()).orElseThrow();

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
