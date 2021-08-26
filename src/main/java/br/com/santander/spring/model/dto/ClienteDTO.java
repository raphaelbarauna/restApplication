package br.com.santander.spring.model.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {

    private Integer idCliente;
    private String nome;
}
