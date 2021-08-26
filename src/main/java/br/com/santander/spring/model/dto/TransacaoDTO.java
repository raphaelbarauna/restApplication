package br.com.santander.spring.model.dto;


import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransacaoDTO {

    private Integer idTransacao;
    private Date dataTransacao;
    private String tipoTransacao;
    private Double valor;
    private ClienteDTO clienteDTO;
}
