package com.example.gioia.service;

import com.example.gioia.eccezioni.UtenteEsistente;
import com.example.gioia.entity.Cliente;
import com.example.gioia.repositories.ClienteRepository;
import com.example.gioia.security.authentication.UserRegistrationRecord;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
    @Slf4j
    public class KeycloakService implements  KeycloakUserService{

        @Autowired
        private ClienteRepository clienteRepository;

        @Override
        @Transactional(rollbackOn = Exception.class)
        public ResponseEntity createUser(UserRegistrationRecord uss) throws Exception, UtenteEsistente {
            if (uss == null) throw new Exception();

            Cliente c= new Cliente();
            c.setNome(uss.nome());
            Cliente nuovo = clienteRepository.save(c);

            Keycloak keycloak= KeycloakBuilder.builder().
                    serverUrl("http://localhost:8180").
                    realm("gioia").
                    grantType(OAuth2Constants.CLIENT_CREDENTIALS).
                    clientId("admin_cli").
                    clientSecret("UubJ2PW7y2i2F7qHJ0wD3sKaVfUl0W5y").
                    username("admin").password("pass").
                    build();

            UserRepresentation user= new UserRepresentation();
            //user.setId(String.valueOf(nuovo.getId_cliente()));
            user.setEnabled(true);
            user.setUsername(uss.username());
            user.setEmail(uss.email());
            user.setFirstName(uss.nome());
            user.setEmailVerified(true);

            CredentialRepresentation credential= new CredentialRepresentation();
            credential.setValue(uss.password());
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);

            List<CredentialRepresentation> list= new ArrayList<>();
            list.add(credential);
            user.setCredentials(list);

            Integer idToSave = nuovo.getId_cliente();
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("UserId", Collections.singletonList(idToSave.toString()));
            attributes.put("origin",Arrays.asList("demo"));
            user.setAttributes(attributes);

            RealmResource realm=keycloak.realm("gioia");
            UsersResource us= realm.users();

            Response response = us.create(user);
            if(response.getStatus()==201){
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                ClientRepresentation clientRep = realm.clients().findByClientId("admin-cli").get(0);
                ClientResource clientResource = realm.clients().get(clientRep.getId());

                RoleRepresentation userRole = clientResource.roles().get("utente").toRepresentation();
                us.get(userId).roles().clientLevel(clientResource.toRepresentation().getId()).add(Collections.singletonList(userRole));
                return new ResponseEntity(nuovo, HttpStatus.OK);
            } else {
                throw new Exception();
        }
    }


    }
