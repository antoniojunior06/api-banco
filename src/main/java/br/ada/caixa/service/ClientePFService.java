package br.ada.caixa.service;

import br.ada.caixa.dto.filter.ClientePFFiltro;
import br.ada.caixa.dto.request.clientePF.AtualizacaoPFRequestDto;
import br.ada.caixa.dto.request.clientePF.InsercaoPFRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.enums.Status;
import br.ada.caixa.exception.ValidacaoException;
import br.ada.caixa.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientePFService {

    final private ClienteRepository clienteRepository;
    final private ModelMapper modelMapper;

    public ClientePFService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
//        this.modelMapper.typeMap(InsercaoPFRequestDto.class, ClientePF.class)
//                .addMapping(InsercaoPFRequestDto::getCpf, ClientePF::setDocumento);
    }

    public ClientePFResponseDto inserir(InsercaoPFRequestDto insercaoPFRequestDto) {
        ClientePF cliente = modelMapper.map(insercaoPFRequestDto, ClientePF.class);
        cliente.setStatus(Status.ATIVO);
        cliente.setDataCadastro(LocalDate.now());
        cliente = clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClientePFResponseDto.class);
    }

    public ClientePFResponseDto atualizar(Long id, AtualizacaoPFRequestDto atualizacaoPFRequestDto) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    modelMapper.map(atualizacaoPFRequestDto, cliente);
                    return clienteRepository.save(cliente);
                })
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));

    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<ClientePFResponseDto> listarTodos() {
        return clienteRepository.findAllPF()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .toList();

    }

    public ClientePFResponseDto buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
    }

    public List<ClientePFResponseDto> buscarPorNome(ClientePFFiltro filter) {
        return clienteRepository.findAllByName(filter.getNome())
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .toList();
    }

    public ClientePFResponseDto buscarPorCpf(ClientePFFiltro filter) {
        return clienteRepository.findByCpf(filter.getCpf())
                .map(cliente -> modelMapper.map(cliente, ClientePFResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
    }
}
