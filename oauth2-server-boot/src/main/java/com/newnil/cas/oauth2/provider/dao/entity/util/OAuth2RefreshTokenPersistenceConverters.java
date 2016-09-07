package com.newnil.cas.oauth2.provider.dao.entity.util;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import javax.persistence.AttributeConverter;

public class OAuth2RefreshTokenPersistenceConverters implements AttributeConverter<OAuth2RefreshToken, String> {

    private JsonPersistenceConverters<OAuth2RefreshToken> jsonPersistenceConverters = new JsonPersistenceConverters<>();

    @Override
    public String convertToDatabaseColumn(OAuth2RefreshToken attribute) {
        return jsonPersistenceConverters.convertToJson(attribute);
    }

    @Override
    public OAuth2RefreshToken convertToEntityAttribute(String dbData) {
        return jsonPersistenceConverters.convertFromJson(dbData, OAuth2RefreshToken.class);
    }
}
