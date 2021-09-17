//package br.com.santander.spring.Kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaProducer {
//    @Value("${topicos.dadoCadastral}")
//    private String topico;
//
//    @Autowired
//    private LogMensageriaDadosCadastraisService logMensageriaDadosCadastraisService;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public void enviarMensagem(DadoCadastralDTO dado, PoloAtivo poloAtivo, Paradigma paradigma) throws Exception{
//
//        String mensagem = new ObjectMapper().writeValueAsString(dado);
//
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topico, mensagem);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                salvarLog("Mensagem enviada=[" + mensagem + "] com offset=[" + result.getRecordMetadata().offset() + "]", poloAtivo, paradigma);
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//
//                salvarLog("Incapaz de mandar a mensagem=[" + mensagem + "] devido a: " + ex.getMessage(), poloAtivo, paradigma);
//            }
//        });
//
//    }
//
//    private void salvarLog(String mensagem, PoloAtivo poloAtivo, Paradigma paradigma) {
//
//        LogMensageriaDadosCadastrais log = LogMensageriaDadosCadastrais.builder()
//                .data(LocalDateTime.now())
//                .mensagem(mensagem)
//                .topico(topico)
//                .paradigma(paradigma)
//                .poloAtivo(poloAtivo)
//                .build();
//
//        logMensageriaDadosCadastraisService.salvar(log);
//    }
//}
