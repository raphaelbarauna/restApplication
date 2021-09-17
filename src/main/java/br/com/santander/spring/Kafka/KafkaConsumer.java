//package br.com.santander.spring.Kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumer {
//
//    @Autowired
//    private DadoCadastralService dadoCadastralService;
//
//    @Value("${topicos.dadoCadastral}")
//    private String topico;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "${topicos.dadoCadastral}")
//    public void listenSAPConsultaCPF(String message) throws Exception {
//
//        objectMapper.registerModule(new JavaTimeModule());
//
//        DadoCadastralDTO dadoCadastralDTO = objectMapper.readValue(message, DadoCadastralDTO.class);
//
//        try{
//            dadoCadastralService.consultarDados(dadoCadastralDTO, message, topico);
//        } catch(Exception e){
//            throw new Exception(e.getLocalizedMessage());
//        }
//    }c
//}
