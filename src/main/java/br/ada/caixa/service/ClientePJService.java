package br.ada.caixa.service;

import br.ada.caixa.dto.filter.ClientePFFilter;
import br.ada.caixa.dto.filter.ClientePJFilter;
import br.ada.caixa.dto.request.clientePJ.AtualizacaoPJRequestDto;
import br.ada.caixa.dto.request.clientePJ.InsercaoPJRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.dto.response.ClientePJResponseDto;
import br.ada.caixa.entity.cliente.ClientePJ;
import br.ada.caixa.enums.Status;
import br.ada.caixa.exception.ValidacaoException;
import br.ada.caixa.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        cliente = clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClientePJResponseDto.class);
    }

    public ClientePJResponseDto atualizar(Long id, AtualizacaoPJRequestDto atualizacaoPJRequestDto) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    modelMapper.map(atualizacaoPJRequestDto, cliente);
                    return clienteRepository.save(cliente);
                })
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));

    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<ClientePJResponseDto> listarTodos() {
        return clienteRepository.findAllPJ()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .toList();

    }

    public ClientePJResponseDto buscarPorId(Long id) {
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

    public ClientePJResponseDto buscarPorCnpj(ClientePJFilter filter) {
        return clienteRepository.findByCnpj(filter.getCnpj())
                .map(cliente -> modelMapper.map(cliente, ClientePJResponseDto.class))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado"));
    }


}
