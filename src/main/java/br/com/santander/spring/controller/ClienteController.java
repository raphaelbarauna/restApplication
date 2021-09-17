package br.com.santander.spring.controller;

import br.com.santander.spring.exception.NoDataFoundException;
import br.com.santander.spring.model.Cliente;
import br.com.santander.spring.model.dto.TransacaoDTO;
import br.com.santander.spring.service.ClienteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@Log4j2
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listar")
    public ResponseEntity<Page<Cliente>> listarClientes(@PageableDefault(page = 0, size = 10, sort = "idCliente", direction = Sort.Direction.ASC) Pageable pageable) {

        var clientes = clienteService.listarClientes(pageable);

        return new ResponseEntity<>(clientes, HttpStatus.OK);

    }

    @GetMapping("/transacoes")
    public ResponseEntity<Page<TransacaoDTO>> listarTransacoesPorData(@PageableDefault(page = 0, size = 10, sort = "idTransacao", direction = Sort.Direction.ASC) Pageable pageable,
                                                                      @RequestParam(required = true) String data) throws ParseException {

        var transacoes = clienteService.listarTransacoesPorData(new SimpleDateFormat("yyyy-MM-dd").parse(data), pageable);

        return new ResponseEntity<>(transacoes, HttpStatus.OK);
    }

    @PostMapping("/salvar")
    public ResponseEntity<Cliente> salvarCliente(@Valid @RequestBody Cliente cliente) {
        try {

            return new ResponseEntity<Cliente>(clienteService.salvar(cliente), HttpStatus.CREATED);

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;

    }

    @PatchMapping("/sacar/{id}")
    public ResponseEntity<Cliente> sacarValor(@PathVariable Integer id, @RequestBody Map<Object, Object> valor) {

        var cliente = clienteService.buscarPorId(id);

        return new ResponseEntity<>(clienteService.sacarValor(cliente, (Double) valor.get("valor")), HttpStatus.OK);

    }

    @PatchMapping("/depositar/{id}")
    public ResponseEntity<Cliente> depositarValor(@PathVariable Integer id, @RequestBody Map<Object, Object> valor) {

        var cliente = clienteService.buscarPorId(id);


        return new ResponseEntity<>(clienteService.depositarValor(cliente, (Double) valor.get("valor")), HttpStatus.OK);
    }
}
