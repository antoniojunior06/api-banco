package br.ada.caixa.service;

import br.ada.caixa.dto.request.DepositoRequestDto;
import br.ada.caixa.dto.request.InvestimentoRequestDto;
import br.ada.caixa.dto.request.SaqueRequestDto;
import br.ada.caixa.dto.request.TransferenciaRequestDto;
import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePJ;
import br.ada.caixa.entity.conta.Conta;
import br.ada.caixa.entity.conta.ContaCorrente;
import br.ada.caixa.entity.conta.ContaInvestimento;
import br.ada.caixa.enums.Status;
import br.ada.caixa.exception.ValidacaoException;
import br.ada.caixa.repository.ClienteRepository;
import br.ada.caixa.repository.ContaRepository;
import br.ada.caixa.utils.Taxas;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ModelMapper modelMapper, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
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
            BigDecimal taxa = saqueRequestDto.getValor().multiply(new BigDecimal(String.valueOf(Taxas.TAXA_SAQUE_TRANSFERENCIA_PJ))).setScale(2, RoundingMode.HALF_UP);
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
            BigDecimal taxa = transferenciaRequestDto.getValor().multiply(new BigDecimal(String.valueOf(Taxas.TAXA_SAQUE_TRANSFERENCIA_PJ))).setScale(2, RoundingMode.HALF_UP);
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDto.getValor()).subtract(taxa));
        } else {
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDto.getValor()));
        }

        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaRequestDto.getValor()));

        contaOrigem = contaRepository.save(contaOrigem);
        contaDestino = contaRepository.save(contaDestino);

        return modelMapper.map(contaOrigem, ContaResponseDto.class);
    }

    @Transactional
    public ContaResponseDto investir(InvestimentoRequestDto investimentoRequestDto) {
        Conta conta = contaRepository.findById(investimentoRequestDto.getNumeroConta())
                .orElseThrow(() -> new ValidacaoException("Conta não encontrada."));

        if (conta instanceof ContaCorrente contaCorrente) {

            if (contaCorrente.getSaldo().compareTo(investimentoRequestDto.getValor()) < 0) {
                throw new ValidacaoException("Saldo insuficiente para investimento.");
            }

            // Verifica se a conta investimento já existe
            Optional<ContaInvestimento> contaInvestimentoOptional = contaCorrente.getCliente().getContas()
                    .stream()
                    .filter(c -> c instanceof ContaInvestimento)
                    .map(c -> (ContaInvestimento) c)
                    .findFirst();

            ContaInvestimento contaInvestimento;
            if (contaInvestimentoOptional.isPresent()) {
                contaInvestimento = contaInvestimentoOptional.get();
            } else {
                // Cria a conta investimento se não existir
                contaInvestimento = new ContaInvestimento();
                contaInvestimento.setDataCriacao(LocalDate.now());
                contaInvestimento.setCliente(contaCorrente.getCliente());
                contaInvestimento.setStatus(Status.ATIVO);
                contaInvestimento.setSaldo(BigDecimal.ZERO);

                contaInvestimento = contaRepository.save(contaInvestimento); // Save the newly created account

                contaCorrente.getCliente().getContas().add(contaInvestimento);
                clienteRepository.save(contaCorrente.getCliente());

            }

            contaCorrente.setSaldo(contaCorrente.getSaldo().subtract(investimentoRequestDto.getValor()));
            contaInvestimento.setSaldo(contaInvestimento.getSaldo().add(investimentoRequestDto.getValor()));

            contaRepository.save(contaCorrente);
            contaRepository.save(contaInvestimento);

            return modelMapper.map(contaCorrente, ContaResponseDto.class);
        } else {
            throw new ValidacaoException("Somente contas correntes podem ser utilizadas para investimentos.");
        }
    }


}
