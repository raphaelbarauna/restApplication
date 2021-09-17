//package br.com.santander.spring.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.cors.CorsConfiguration;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Arrays;
//import java.util.Base64;
//
//public class cWebSecurityLDAP  extends WebSecurityConfigurerAdapter {
//        private static final byte[] KEY = {85, -25, 60, 0, 20, 55, 70, 58, 95, 46, -85, 55, 21, -26, -56, 25};
//        private static final String METODO_ENCRIPTACAO = "AES";
//        private static final String LDAP_PATH = "ldap://";
//        private static final String NOOP_123456 = "{noop}123456";
//        private final SecretKeySpec skeySpec = new SecretKeySpec(KEY, METODO_ENCRIPTACAO);
//
//        public static final String LOGIN = "/api/login";
//        public static final String CONFIGURACOES = "/api/configuracoes/routesByRole";
//        public static final String FILIAIS = "/filial/all";
//        public static final String OBRIGACOES = "/api/obrigacoes";
//
//        private UsuariosServ usuariosServ;
//        @Autowired
//        public void setUsuariosServ(UsuariosServ value) {
//            this.usuariosServ = value;
//        }
//
//        private ConfiguracoesServ configuracoesServ;
//        @Autowired
//        public void setConfiguracoesServ(ConfiguracoesServ value) {
//            this.configuracoesServ = value;
//        }
//
//        private Environment enviroment;
//        @Autowired
//        public void setEnviroment(Environment value) {
//            this.enviroment = value;
//        }
//
//        @Bean
//        CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true);
//
//            config.setAllowedOrigins(Arrays.asList("http://0.0.0.0:8081", "http://localhost:8081", "http://localhost:8080", "http://localhost:8088", "http://192.168.0.240:8081", "http://www.kyros.com.br", "http://localhost:7001", "http://localhost:7002", "http://10.129.227.12:7001", "http://10.129.227.12:7002"));
//
//            config.addAllowedHeader("*");
//            config.addAllowedMethod("*");
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", config);
//            return source;
//        }
//
//        @Override
//        public void configure(WebSecurity web) {
//
//            web.ignoring().antMatchers("/*", "/css/**", "/fonts/**", "/img/**", "/js/**", "/configuration/ui",
//                    "/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/configuration/security/**",
//                    "/v2/api-docs", CONFIGURACOES, FILIAIS, OBRIGACOES); // static resources
//
//
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            List<RoutesByRoles> routesByRoles = configuracoesServ.getRoutesByRole();
//
//
//            log.info("Configurando HttpSecurity");
//            http
//                    .cors()
//                    .and()
//                    .csrf()
//                    .ignoringAntMatchers(LOGIN).
//                    ignoringAntMatchers(CONFIGURACOES).csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    .ignoringAntMatchers(FILIAIS).csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    .ignoringAntMatchers(OBRIGACOES).csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    .and().authorizeRequests().antMatchers(CONFIGURACOES).permitAll()
//                    .and().authorizeRequests().antMatchers(FILIAIS).permitAll()
//                    .and().authorizeRequests().antMatchers(OBRIGACOES).permitAll()
//                    .and().authorizeRequests()
//                    .and()
//                    .addFilterBefore(new JWTLoginFilter(LOGIN, authenticationManager(),  configuracoesServ,usuariosServ), UsernamePasswordAuthenticationFilter.class)
//                    /*             .ignoringAntMatchers(LOGIN)
//                                 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                                 .and()
//                                 .addFilterBefore(new JWTLoginFilter("/api/login", authenticationManager(), configuracoesServ,usuariosServ), UsernamePasswordAuthenticationFilter.class)*/
//                    .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//            for (RoutesByRoles routes : routesByRoles){
//                String [] roules =routes.getRoules().split(",");
//
//                http.authorizeRequests()
//                        .antMatchers(routes.getRoutes()).hasAnyAuthority(roules);
//            }
//            log.info("HttpSecurity configurado");
//        }
//
//        @Override
//        public void configure(AuthenticationManagerBuilder auth) throws Exception{
//            log.info("Iniciando configuracao de autenticacao");
//            Configuracoes configs = configuracoesServ.getConfig("LDAP");
//            if (configs != null && !Arrays.asList(enviroment.getActiveProfiles()).contains("dev")) {
//                try {
//                    log.info("Iniciando configuracao do LDAP {}", LDAP_PATH + configs.getHostname());
//                    auth
//                            .ldapAuthentication()
//                            .userSearchFilter(configs.getUserSearchFilter())
//                            .userSearchBase(configs.getUserSearchBase())
//                            .groupSearchBase(configs.getGroupSearchBase())
//                            .groupSearchFilter(configs.getGroupSearchFilter())
//                            .contextSource()
//                            .url(LDAP_PATH + configs.getHostname())
//                            .port(configs.getPorta())
//                            .managerDn(configs.getUsuarioDmAdministrador())
//                            .managerPassword(decrypt(configs.getSenhaDmAdministrador()));
//                    log.info("Acesso configurado para LDAP {}", LDAP_PATH + configs.getHostname());
//                } catch (Exception e) {
//                    log.error("Problemas na conexão com o domínio LDAP", e);
//                    throw e;
//                }
//            } else {
//                try {
//                    auth
//                            .inMemoryAuthentication()
//                            .withUser("admin").password(NOOP_123456)
//                            .authorities(Roles.GIA, Roles.SPED
//                                  )
//                            .and()
//                            .withUser("admin_only").password(NOOP_123456).authorities(Roles.ROLE_GF_POF_ADM)
//                            .and()
//                            .withUser("ti").password(NOOP_123456).authorities(Roles.ROLE_)
//                            .and()
//                            .withUser("rel_ac24").password(NOOP_123456).authorities(Roles.ROLE_,Roles.ROLE_AC24)
//                    ;
//
//                    log.info("Acesso configurado para usuário admin padrão");
//                } catch (Exception ex) {
//                    log.error("Problemas na configuração do usuario local", ex);
//                }
//            }
//        }
//
//        //Método utlizado para decriptação de senhas para LDAP
//        private String decrypt(String pw) {
//            try {
//                byte[] decrypted;//decalra um array de bytes
//                byte[] decoded = Base64.getDecoder().decode(pw);//decalra um array de bytes que recebe a vari?vel que havia sido serializada de forma criptogrfada
//                Cipher cipher = Cipher.getInstance(METODO_ENCRIPTACAO);//Inicializa objeto Cipher
//                cipher.init(Cipher.DECRYPT_MODE, skeySpec);//inicializa o Cipher no modo de decripta??o e passa a chave que havia sido definida
//                decrypted = cipher.doFinal(decoded);//utiliza o cipher para decriptar a senha do banco de dados
//                pw = new String(decrypted);
//                log.info("Senha descriptografada!");
//                return pw;
//            } catch (Exception e) {
//                log.error("Erro ao descriptografar!", e);
//                return null;
//            }
//        }
//}
