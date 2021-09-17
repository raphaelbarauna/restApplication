package br.com.santander.spring.service;

import br.com.santander.spring.model.Cliente;
import br.com.santander.spring.model.Transacao;
import br.com.santander.spring.repository.ClienteRepository;
import br.com.santander.spring.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ClienteService clienteService;


    private Cliente clienteBuilder() {
        return new Cliente(1, "Santander", false, 1000.00, 2508202, new Date());

    }

    private Transacao transacaoSaqueBuilder() throws ParseException {

        Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");

        return new Transacao(data, "SAQUE", 200.00, clienteBuilder());

    }

    private Transacao transacaoDepositoBuilder() throws ParseException {

        Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");

        return new Transacao(data, "DEPOSITO", 200.00, clienteBuilder());

    }

    @Test
    void testarMetodoListarClientesPassandoPageable() {

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Santander", true, 100.00, 2508202, new Date()));
        clientes.add(new Cliente(2, "Capgemini", true, 100.00, 2508202, new Date()));

        var pageable = PageRequest.of(0, 10, Sort.by("idCliente").ascending());

        Page<Cliente> clientePage = new PageImpl<>(clientes);

       when(clienteRepository.findAll(isA(Pageable.class))).thenReturn(clientePage);

        assertEquals("Capgemini", clienteService.listarClientes(pageable).getContent().get(1).getNome());

    }

    @Test
    void testarMetodoSalvarCliente() {

        var cliente = clienteBuilder();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        assertEquals("Santander", clienteService.salvar(cliente).getNome());
    }

    @Test
    void testarMetodoBuscarPorId() {

        var cliente = Optional.of(clienteBuilder());

        when(clienteRepository.findById(1)).thenReturn(cliente);

        assertEquals(clienteService.buscarPorId(1), cliente);
    }

    @Test
    void testarMetodoSacarValorPassandoClienteEValorMenorQueCem() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);


        var clienteSalvo = clienteService.sacarValor(cliente, 90.00);

        assertEquals(910.0, clienteSalvo.getSaldo());
    }

    @Test
    void testarMetodoSacarValorPassandoClienteEValorBaixoSaque() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ReflectionTestUtils.setField(clienteService, "taxaSaqueBaixo", 0.4);

        var clienteSalvo = clienteService.sacarValor(cliente, 200.00);

        assertEquals(799.2, clienteSalvo.getSaldo());
    }

    @Test
    void testarMetodoSacarValorPassandoClienteEValorAltoSaque() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ReflectionTestUtils.setField(clienteService, "taxaSaqueAlto", 1.0);
        var clienteSalvo = clienteService.sacarValor(cliente, 400.00);

        assertEquals(596.0, clienteSalvo.getSaldo());
    }

    @Test
    void testarMetodoDepositarValorPassandoClienteEValorDeposito() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoDepositoBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clienteSalvo = clienteService.depositarValor(cliente, 400.00);

        assertEquals(1400.0, clienteSalvo.getSaldo());
    }

    @Test
    void testarMetodolistarTransacoesPorData() throws ParseException {

        List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(transacaoSaqueBuilder());
        transacoes.add(transacaoDepositoBuilder());

        var pageable = PageRequest.of(0, 10, Sort.by("idTransacao").ascending());

        Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");

        Page<Transacao> transacaoPage = new PageImpl<>(transacoes);

        when(transacaoRepository.findAllByDataTransacao(data, pageable)).thenReturn(transacaoPage);

        var transacoesDTO = clienteService.listarTransacoesPorData(data, pageable);

        assertEquals(transacoesDTO.getContent().get(0).getTipoTransacao(), "SAQUE");
    }

}
