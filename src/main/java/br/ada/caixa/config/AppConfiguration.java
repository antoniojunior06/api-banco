package br.ada.caixa.config;

import br.ada.caixa.dto.request.clientePF.InsercaoPFRequestDto;
import br.ada.caixa.dto.request.clientePJ.InsercaoPJRequestDto;
import br.ada.caixa.dto.response.ClientePFResponseDto;
import br.ada.caixa.dto.response.ClientePJResponseDto;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.entity.cliente.ClientePJ;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper getModelMapper() {


        var modelMapper = new ModelMapper();

        modelMapper.typeMap(InsercaoPFRequestDto.class, ClientePF.class)
                .addMapping(InsercaoPFRequestDto::getCpf, ClientePF::setDocumento);

        modelMapper.typeMap(ClientePF.class, ClientePFResponseDto.class)
                .addMapping(ClientePF::getDocumento, ClientePFResponseDto::setCpf);

        modelMapper.typeMap(InsercaoPJRequestDto.class, ClientePJ.class)
                .addMapping(InsercaoPJRequestDto::getCnpj, ClientePJ::setDocumento);

        modelMapper.typeMap(ClientePJ.class, ClientePJResponseDto.class)
                .addMapping(ClientePJ::getDocumento, ClientePJResponseDto::setCnpj);

        return modelMapper;
    }

}
