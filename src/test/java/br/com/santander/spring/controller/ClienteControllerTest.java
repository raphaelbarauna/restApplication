package br.com.santander.spring.controller;


import br.com.santander.spring.model.Cliente;
import br.com.santander.spring.repository.ClienteRepository;
import br.com.santander.spring.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListarTodosClientes() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/listar")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "idCliente")
                .param("direction", Sort.Direction.ASC.toString())
        ).andDo(print())// <-- no space after comma!
                .andExpect(status().isOk());

    }

}
