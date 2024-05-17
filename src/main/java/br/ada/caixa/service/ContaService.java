package br.ada.caixa.service;

import br.ada.caixa.dto.response.ContaResponseDto;
import br.ada.caixa.repository.ContaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    final private ContaRepository contaRepository;
    final private ModelMapper modelMapper;

    public ContaService(ContaRepository contaRepository, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.modelMapper = modelMapper;
    }



}
