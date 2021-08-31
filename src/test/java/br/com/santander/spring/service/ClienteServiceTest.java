package br.com.santander.spring.service;

import br.com.santander.spring.model.Cliente;
import br.com.santander.spring.model.Transacao;
import br.com.santander.spring.repository.ClienteRepository;
import br.com.santander.spring.repository.TransacaoRepository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

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
    public void testarMetodoListarClientesPassandoPageable() {

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Santander", true, 100.00, 2508202, new Date()));
        clientes.add(new Cliente(2, "Capgemini", true, 100.00, 2508202, new Date()));

        var pageable = PageRequest.of(0, 10, Sort.by("idCliente").ascending());

        Page<Cliente> clientePage = new PageImpl<>(clientes);

       when(clienteRepository.findAll(isA(Pageable.class))).thenReturn(clientePage);

        assertEquals(clienteService.listarClientes(pageable).getContent().get(1).getNome(), "Capgemini");

    }

    @Test
    public void testarMetodoSalvarCliente() {

        var cliente = clienteBuilder();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        assertEquals(clienteService.salvar(cliente).getNome(), "Santander");
    }

    @Test
    public void testarMetodoBuscarPorId() {

        var cliente = Optional.of(clienteBuilder());

        when(clienteRepository.findById(1)).thenReturn(cliente);

        assertEquals(clienteService.buscarPorId(1), cliente);
    }

    @Test
    public void testarMetodoSacarValorPassandoClienteEValorMenorQueCem() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);


        var clienteSalvo = clienteService.sacarValor(cliente, 90.00);

        assertEquals(clienteSalvo.getSaldo(), 910.0);
    }

    @Test
    public void testarMetodoSacarValorPassandoClienteEValorBaixoSaque() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ReflectionTestUtils.setField(clienteService, "taxaSaqueBaixo", 0.4);

        var clienteSalvo = clienteService.sacarValor(cliente, 200.00);

        assertEquals(clienteSalvo.getSaldo(), 799.2);
    }

    @Test
    public void testarMetodoSacarValorPassandoClienteEValorAltoSaque() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoSaqueBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ReflectionTestUtils.setField(clienteService, "taxaSaqueAlto", 1.0);
        var clienteSalvo = clienteService.sacarValor(cliente, 400.00);

        assertEquals(clienteSalvo.getSaldo(), 596.0);
    }

    @Test
    public void testarMetodoDepositarValorPassandoClienteEValorDeposito() throws ParseException {

        var cliente = clienteBuilder();
        var transacao = transacaoDepositoBuilder();

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clienteSalvo = clienteService.depositarValor(cliente, 400.00);

        assertEquals(clienteSalvo.getSaldo(), 1400.0);
    }

    @Test
    public void testarMetodolistarTransacoesPorData() throws ParseException {

        List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(transacaoSaqueBuilder());
        transacoes.add(transacaoDepositoBuilder());

        var pageable = PageRequest.of(0, 10, Sort.by("idTransacao").ascending());

        Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");

        Page<Transacao> transacaoPage = new PageImpl<>(transacoes);

        when(transacaoRepository.findAllByDataTransacao(data, pageable)).thenReturn(transacaoPage);

        var transacoesDTO = clienteService.listarTransacoesPorData(data, pageable);

        assertTrue(transacoesDTO.getContent().get(0).getTipoTransacao().equals("SAQUE"));
    }

}
