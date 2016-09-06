package com.newnil.cas.oauth2.provider.service;

import com.newnil.cas.oauth2.provider.dao.entity.*;
import com.newnil.cas.oauth2.provider.dao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuth2DatabaseClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Autowired
    private GrantTypeRepository grantTypeRepository;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private ResourceIdRepository resourceIdRepository;

    @Autowired
    private RedirectUriRepository redirectUriRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientDetailsRepository.findOneByClientId(clientId).map(entityToDomain).orElseThrow(() -> new NoSuchClientException("Client ID not found"));
    }

    @Transactional
    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        if (clientDetailsRepository.findOneByClientId(clientDetails.getClientId()).isPresent()) {
            throw new ClientAlreadyExistsException("Client ID already exists");
        }


        ClientDetailsEntity clientDetailsEntity = ClientDetailsEntity.builder()
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds    (clientDetails.getRefreshTokenValiditySeconds())
                .build();

        // 如果下面这段编译报错请升级jdk
        // OAuth2DatabaseClientDetailsService.java:[64,30] unreported exception X; must be caught or declared to be thrown
        // 或者将 ").orElseThrow(" 修改成 ").<ClientRegistrationException>orElseThrow("
        // http://stackoverflow.com/a/25533461

        clientDetailsEntity.setAuthorizedGrantTypeXrefs(clientDetails.getAuthorizedGrantTypes().stream().map(
                grantType -> grantTypeRepository.findOneByValue(grantType).map(
                        grantTypeEntity -> ClientDetailsToAuthorizedGrantTypeXrefEntity.builder()
                                .clientDetails(clientDetailsEntity)
                                .grantType(grantTypeEntity)
                                .build()
                ).<ClientRegistrationException>orElseThrow(() -> new ClientRegistrationException("Unsupported grant type: " + grantType))
        ).collect(Collectors.toSet()));

        clientDetailsEntity.setScopeXrefs(clientDetails.getScope().stream().map(
                scope -> scopeRepository.findOneByValue(scope).map(
                        scopeEntity -> ClientDetailsToScopesXrefEntity.builder()
                                .clientDetails(clientDetailsEntity)
                                .scope(scopeEntity)
                                .autoApprove(clientDetails.isAutoApprove(scope))
                                .build()
                ).<ClientRegistrationException>orElseThrow(() -> new ClientRegistrationException("Unknown scope: " + scope))
        ).collect(Collectors.toSet()));

        clientDetailsEntity.setResourceIdXrefs(clientDetails.getResourceIds().stream().map(
                resourceId -> resourceIdRepository.findOneByValue(resourceId).map(
                        resourceIdEntity -> ClientDetailsToResourceIdXrefEntity.builder()
                                .clientDetails(clientDetailsEntity)
                                .resourceId(resourceIdEntity)
                                .build()
                ).<ClientRegistrationException>orElseThrow(() -> new ClientRegistrationException("Unknown resource id: " + resourceId))
        ).collect(Collectors.toSet()));

        ClientDetailsEntity savedEntity = clientDetailsRepository.save(clientDetailsEntity);

        redirectUriRepository.save(clientDetails.getRegisteredRedirectUri().stream().map(
                redirectUri -> RedirectUriEntity.builder()
                        .clientDetails(savedEntity)
                        .value(redirectUri)
                        .build()
        ).collect(Collectors.toSet()));
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {

    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }

    private final Function<? super ClientDetailsEntity, ? extends BaseClientDetails> entityToDomain = entity -> {
        BaseClientDetails clientDetails = new BaseClientDetails();

        clientDetails.setClientId(entity.getClientId());
        clientDetails.setClientSecret(entity.getClientSecret());

        clientDetails.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());

        clientDetails.setAuthorizedGrantTypes(entity.getAuthorizedGrantTypeXrefs().stream().map(
                grantTypeXrefEntity -> grantTypeXrefEntity.getGrantType().getValue()).collect(Collectors.toList()));

        clientDetails.setScope(entity.getScopeXrefs().stream().map(
                scopeXrefEntity -> scopeXrefEntity.getScope().getValue()).collect(Collectors.toList()));

        clientDetails.setAutoApproveScopes(entity.getScopeXrefs().stream().filter(ClientDetailsToScopesXrefEntity::getAutoApprove)
                .map(scopeXrefEntity -> scopeXrefEntity.getScope().getValue()).collect(Collectors.toList()));

        clientDetails.setResourceIds(entity.getResourceIdXrefs().stream().map(resXref -> resXref.getResourceId().getValue()).collect(Collectors.toList()));

        clientDetails.setRegisteredRedirectUri(entity.getRedirectUris().stream().map(RedirectUriEntity::getValue).collect(Collectors.toSet()));

        clientDetails.setAdditionalInformation(Collections.<String, Object>emptyMap());

        return clientDetails;
    };

}
