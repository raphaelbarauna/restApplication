//package br.com.santander.spring.config;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Optional;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityOAM {
//    private static final String PREFIXO_PERFIL = "ROLE_";
//    private static final String ATRIBUTO_PERFIS = "";
//
//    private static final String SCOPE_SISTEMICO = "ServiceAccount.Profile";
//
//    @Autowired
//    private OAMServiceClient oamServiceClient;
//
//    @Autowired
//    private UsuarioService usuarioService;
//
//    @Autowired
//    private AclClient aclClient;
//
//    @Value("${oam.sap.clientId}")
//    private String sapClientID;
//
//    @Value("${oam.sap.perfil}")
//    private String sapPerfil;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .csrf().disable()
//                .headers()
//                .frameOptions()
//                .disable()
//                .and()
////            .antMatcher("/disable")
//                .oauth2ResourceServer()
//                .jwt()
//                .jwtAuthenticationConverter(new JwtAuthenticationConverter()
//                {
//                    @Override
//                    @SuppressWarnings({"unchecked", "deprecation"})
//                    //Utilizado para identificar no token quais são os perfis concedidos para o usuário
//                    //e adiciona como authority. Depois disso é possível proteger os endpoints com
//                    //a anotação @PreAuthorize("hasRole('ADMIN')")
//                    protected Collection<GrantedAuthority> extractAuthorities(final Jwt jwt){
//
//                        String scope = jwt.getClaimAsString("oracle.oauth.scope");
//                        String clientID =  jwt.getClaimAsString("oracle.oauth.client_origin_id");
//                        String login = jwt.getClaimAsString("sub");
//
//                        Collection<GrantedAuthority> authorities = super.extractAuthorities(jwt);
//
//                        if(StringUtils.isNotEmpty(scope) && scope.contains(SCOPE_SISTEMICO)){
//                            //usuario sistemico
//                            if(clientID.equals(sapClientID)){
//                                //USUARIO SAP
//                                authorities.add(new SimpleGrantedAuthority(PREFIXO_PERFIL + sapPerfil));
//
//                                usuarioService.salvarUsuarioServico(login, login, PREFIXO_PERFIL + sapPerfil);
//                            }
//                        }else{
//
//                            //Recupera os perfils no OAM
//                            LinkedHashMap usuarioHashMap = oamServiceClient.getToken(jwt.getTokenValue());
//
//                            try{
//                                String perfil = (String) Optional.ofNullable(usuarioHashMap.get(ATRIBUTO_PERFIS)).orElse(null);
//                                definirRecursosProtegidosPorPerfil(authorities, perfil, jwt.getTokenValue());
//                                authorities.add(new SimpleGrantedAuthority(PREFIXO_PERFIL + perfil));
//                            }catch (ClassCastException ex){
//                                List<String> perfils = (ArrayList<String>) Optional.ofNullable(usuarioHashMap.get(ATRIBUTO_PERFIS)).orElse(null);
//                                perfils.forEach(per -> {
//                                    definirRecursosProtegidosPorPerfil(authorities, per, jwt.getTokenValue());
//                                    authorities.add(new SimpleGrantedAuthority(PREFIXO_PERFIL + per ));
//                                });
//                            }
//                            usuarioService.salvarUsuarios(usuarioHashMap);
//                        }
//
//                        return authorities;
//
//                    }
//
//                    private void definirRecursosProtegidosPorPerfil(Collection<GrantedAuthority> authorities, String perfil, String token) {
//                        PerfilDTO perfilDTO = aclClient.getPerfil(perfil,"Bearer "+token);
//                        if(perfilDTO !=null){
//                            perfilDTO.getRecursosProtegidos().forEach(p ->{
//                                authorities.add(new SimpleGrantedAuthority(PREFIXO_PERFIL + p.getNome()));
//                            });
//                        }
//                    }
//                });
//        // @formatter:on
//    }
//}
