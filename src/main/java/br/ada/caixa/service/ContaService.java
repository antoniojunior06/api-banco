package br.ada.caixa.service;

import br.ada.caixa.dto.request.DepositoRequestDto;
import br.ada.caixa.dto.request.SaqueRequestDto;
import br.ada.caixa.dto.request.TransferenciaRequestDto;
import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePJ;
import br.ada.caixa.entity.conta.Conta;
import br.ada.caixa.exception.ValidacaoException;
import br.ada.caixa.repository.ContaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ModelMapper modelMapper;

    public ContaService(ContaRepository contaRepository, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.modelMapper = modelMapper;
    }

    public ContaResponseDto depositar(DepositoRequestDto depositoRequestDto) {
        Conta conta = contaRepository.findById(depositoRequestDto.getNumeroConta())
                .orElseThrow(() -> new ValidacaoException("Conta não localizada"));

        conta.setSaldo(conta.getSaldo().add(depositoRequestDto.getValor()));
        conta = contaRepository.save(conta);

        return modelMapper.map(conta, ContaResponseDto.class);

    }

    public ContaResponseDto consultarSaldo(Long numeroConta) {
        Conta conta = contaRepository.findById(numeroConta)
                .orElseThrow(() -> new ValidacaoException("Conta não localizada"));

        return modelMapper.map(conta, ContaResponseDto.class);
    }

    public ContaResponseDto sacar(SaqueRequestDto saqueRequestDto) {
        Conta conta = contaRepository.findById(saqueRequestDto.getNumeroConta())
                .orElseThrow(() -> new ValidacaoException("Conta não localizada"));

        if (conta.getSaldo().compareTo(saqueRequestDto.getValor()) < 0) {
            throw new ValidacaoException("Saldo insuficiente");
        }

        Cliente cliente = conta.getCliente();

        if (cliente instanceof ClientePJ) {
            BigDecimal taxa = saqueRequestDto.getValor().multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_UP);
            conta.setSaldo(conta.getSaldo().subtract(saqueRequestDto.getValor()).subtract(taxa));
        } else {
            conta.setSaldo(conta.getSaldo().subtract(saqueRequestDto.getValor()));
        }

        conta = contaRepository.save(conta);

        return modelMapper.map(conta, ContaResponseDto.class);

    }

    public ContaResponseDto transferir(TransferenciaRequestDto transferenciaRequestDto) {
        Conta contaOrigem = contaRepository.findById(transferenciaRequestDto.getNumeroContaOrigem())
                .orElseThrow(() -> new ValidacaoException("Conta não localizada"));

        Conta contaDestino = contaRepository.findById(transferenciaRequestDto.getNumeroContaDestino())
                .orElseThrow(() -> new ValidacaoException("Conta não localizada"));

        if (contaOrigem.getSaldo().compareTo(transferenciaRequestDto.getValor()) < 0) {
            throw new ValidacaoException("Saldo insuficiente");
        }

        Cliente cliente = contaOrigem.getCliente();

        if (cliente instanceof ClientePJ) {
            BigDecimal taxa = transferenciaRequestDto.getValor().multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_UP);
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDto.getValor()).subtract(taxa));
        } else {
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDto.getValor()));
        }

        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaRequestDto.getValor()));

        contaOrigem = contaRepository.save(contaOrigem);
        contaDestino = contaRepository.save(contaDestino);

        return modelMapper.map(contaOrigem, ContaResponseDto.class);
    }

}
