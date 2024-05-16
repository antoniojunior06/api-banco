package br.ada.caixa.service;

import br.ada.caixa.dto.request.ContaRequestDto;
import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.entity.conta.ContaCorrente;
import br.ada.caixa.repository.ContaCorrenteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ContaCorrenteService {

    final private ContaCorrenteRepository contaCorrenteRepository;
    final private ModelMapper modelMapper;

    public ContaCorrenteService(ContaCorrenteRepository contaCorrenteRepository, ModelMapper modelMapper) {
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.modelMapper = modelMapper;
    }

    public ContaResponseDto inserir(ContaRequestDto contaRequestDto) {
        ContaCorrente contaCorrente = modelMapper.map(contaRequestDto, ContaCorrente.class);
        contaCorrente = contaCorrenteRepository.save(contaCorrente);
        return modelMapper.map(contaCorrente, ContaResponseDto.class);
    }
}
