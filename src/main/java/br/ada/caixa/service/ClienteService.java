package br.ada.caixa.service;

import br.ada.caixa.dto.response.ClienteResponseDto;
import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    final private ClienteRepository clienteRepository;
    final private ModelMapper modelMapper;

    public ClienteService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;

    }

    public List<ClienteResponseDto> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(cliente -> {
            ClienteResponseDto clienteResponseDto = modelMapper.map(cliente, ClienteResponseDto.class);
            clienteResponseDto.setTipo((cliente instanceof ClientePF) ? "PF" : "PJ");
            return clienteResponseDto;
        }).collect(Collectors.toList());
    }


}
