package br.com.santander.spring.service;

import br.com.santander.spring.exception.ServiceException;
import br.com.santander.spring.model.Cliente;
import br.com.santander.spring.model.Transacao;
import br.com.santander.spring.model.dto.ClienteDTO;
import br.com.santander.spring.model.dto.TransacaoDTO;
import br.com.santander.spring.repository.ClienteRepository;
import br.com.santander.spring.repository.TransacaoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Value("${taxa.saque.baixo}")
    private double taxaSaqueBaixo;

    @Value("${taxa.saque.alto}")
    private double taxaSaqueAlto;

    public Page<Cliente> listarClientes(Pageable pageable) {

        return clienteRepository.findAll(pageable);

    }

    public Cliente salvar(Cliente cliente) {

        return clienteRepository.save(cliente);

    }

    public Optional<Cliente> buscarPorId(Integer id) {

        return clienteRepository.findById(id);

    }

    public Cliente sacarValor(Cliente cliente, Double valorSaque) {

        var taxa = 0.0;

        if ((cliente.getSaldo() - valorSaque) < 0) {
            log.info("Nao tem saldo suficiente");
            throw new ServiceException("Saldo insuficiente.");
        }

        if (valorSaque <= 100.00 || cliente.getExclusivePlan())
            cliente.setSaldo(cliente.getSaldo() - valorSaque);


        if (valorSaque > 100.00 && valorSaque <= 300.00 && !cliente.getExclusivePlan()) {
            taxa = valorSaque * (taxaSaqueBaixo / 100.0);
            cliente.setSaldo(cliente.getSaldo() - valorSaque - taxa);
        }

        if (valorSaque > 300 && !cliente.getExclusivePlan()) {
            taxa = valorSaque * (taxaSaqueAlto / 100.0);
            cliente.setSaldo(cliente.getSaldo() - valorSaque - taxa);
        }

        transacaoRepository.save(new Transacao(new Date(), "SAQUE", valorSaque + taxa, cliente));

        return clienteRepository.save(cliente);
    }

    public Cliente depositarValor(Cliente cliente, Double valorDeposito) {

        cliente.setSaldo(cliente.getSaldo() + valorDeposito);

        transacaoRepository.save(new Transacao(new Date(), "DEPOSITO", valorDeposito, cliente));

        return clienteRepository.save(cliente);
    }

    public Page<TransacaoDTO> listarTransacoesPorData(Date date, Pageable pageable) {

        var transacoes = transacaoRepository.findAllByDataTransacao(date, pageable);

        List<TransacaoDTO> transacaoDTOList = new ArrayList<>();

        transacoes.stream().forEach(item -> transacaoDTOList.add(new TransacaoDTO(
                item.getIdTransacao(),
                item.getDataTransacao(),
                item.getTipoTransacao(),
                item.getValor(),
                new ClienteDTO(item.getCliente().getIdCliente(), item.getCliente().getNome()))));

        return new PageImpl<>(transacaoDTOList);
    }

}
