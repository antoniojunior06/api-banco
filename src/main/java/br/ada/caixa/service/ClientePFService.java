package br.ada.caixa.service;

import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.request.clientePF.AtualizacaoPFRequestDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.entity.conta.ContaCorrente;
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
        contaCorrente.setNumero(new Random().nextInt());
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
//
//    public ClientePFResponseDto buscarPorCpf(ClientePFFilter filter) {
//        return clienteRepository.findByCpf(filter.getCpf())
//                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
//                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
//    }
//
//    public ClientePFResponseDto criarContaPoupanca(InsercaoPFRequestDto insercaoPFRequestDto) {
//        ClientePF cliente = modelMapper.map(insercaoPFRequestDto, ClientePF.class);
//
//        var contaPoupanca = new ContaPoupanca();
//        contaPoupanca.setNumero(new Random().nextInt());
//        contaPoupanca.setDataCriacao(LocalDate.now());
//        contaPoupanca.setCliente(cliente);
//        contaPoupanca.setSaldo(BigDecimal.ZERO);
//        contaPoupanca.setStatus(Status.ATIVO);
//
//        cliente.setContas(new ArrayList<>());
//        cliente.getContas().add(contaPoupanca);
//
//        cliente = clienteRepository.save(cliente);
//
//        return modelMapper.map(cliente, ClientePFResponseDto.class);
//    }

}
