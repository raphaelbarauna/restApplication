# Aplicação Desenvolvida Utilizando JAVA 11 com Spring Boot na versão 2.3.4.

BANCO DE DADOS : H2 ou MYSQL (alterar no application.properties)

Para rodar o projeto basta utilizar o comando : spring-boot:run

ENDPOINTS :

GET :

  - Para listar todos os clientes : http://localhost:8080/clientes/listar
  - Para listar todas transações por data : http://localhost:8080/clientes/transacoes?data=2021-08-25

POST :

  - http://localhost:8080/clientes/salvar 
  
 {
   "nome" : "Rosangela",
   "exclusivePlan" : false,
   "saldo" : 1000.0,
   "numeroConta": 12313213,
   "dataNascimento" : "2001-03-29"
}

PATCH : 

  - http://localhost:8080/clientes/sacar/1

  {
   "valor" : 200.00
  }
  
  - http://localhost:8080/clientes/depositar/1

  {
   "valor" : 404.00
  }
