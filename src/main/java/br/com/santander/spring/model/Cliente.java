package br.com.santander.spring.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@SequenceGenerator(sequenceName = "CLIENTE_SEQ", allocationSize = 1, name = "CLIENTE_SEQ")
    @Column(name = "ID_CLIENTE")
    private Integer idCliente;

    @NotBlank(message = "Nome é Obrigatorio.")
    @Column(name = "NOME")
    private String nome;

    @NotNull
    @Column(name = "PLANO_EXCLUSIVO")
    private Boolean exclusivePlan;

    @Column(name = "SALDO")
    private Double saldo;

    @Column(name = "NUMERO_CONTA")
    private Integer numeroConta;

    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;

    public void setSaldo(Double saldo) {

        if(saldo == null)
            this.saldo = 0.0;
        else
            this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nome='" + nome + '\'' +
                ", exclusivePlan=" + exclusivePlan +
                ", saldo=" + saldo +
                ", numeroConta=" + numeroConta +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
