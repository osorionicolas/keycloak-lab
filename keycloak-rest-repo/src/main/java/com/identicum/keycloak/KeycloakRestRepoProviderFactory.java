package com.identicum.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import static org.keycloak.provider.ProviderConfigProperty.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static org.jboss.logging.Logger.getLogger;

public class KeycloakRestRepoProviderFactory implements UserStorageProviderFactory<KeycloakRestRepoProvider> {

	private static final Logger logger = getLogger(KeycloakRestRepoProviderFactory.class);
	private List<ProviderConfigProperty> configMetadata;
	private RestHandler restHandler;
	private Map<String, Timer> httpStats = new HashMap<String, Timer>();
	
	@Override
	public void init(Scope config) {
		logger.infov("Initializing Keycloak Rest Repo factory version: " + getClass().getPackage().getImplementationVersion());

		ProviderConfigurationBuilder builder = ProviderConfigurationBuilder.create();

		builder.property().name("PROPERTY_BASE_URL")
				.type(STRING_TYPE).label("Base URL")
				.defaultValue("http://umsXX.claro.amx/ums/api")
				.helpText("Api url base to authenticate users")
				.add();
		builder.property().name("PROPERTY_MAX_HTTP_CONNECTIONS")
				.type(STRING_TYPE).label("Max pool connections")
				.defaultValue("50")
				.helpText("Max http connections in pool")
				.add();
		builder.property().name("PROPERTY_SOCKET_TIMEOUT")
				.type(STRING_TYPE).label("HTTP socket timeout")
				.defaultValue("1500")
				.helpText("HTTP socket timeout in milliseconds")
				.add();
		builder.property().name("PROPERTY_CONNECT_TIMEOUT")
				.type(STRING_TYPE).label("HTTP connect timeout")
				.defaultValue("500")
				.helpText("HTTP connect timeout in milliseconds")
				.add();
		builder.property().name("PROPERTY_CONNECTION_REQUEST_TIMEOUT")
				.type(STRING_TYPE).label("HTTP connection request timeout")
				.defaultValue("2000")
				.helpText("HTTP connection request timeout in milliseconds")
				.add();
		builder.property().name("PROPERTY_COUNTRY")
				.type(STRING_TYPE).label("Country")
				.defaultValue("AR")
				.helpText("Two first letters of the country name receiving requests")
				.add();
		builder.property().name("PROPERTY_OAUTH_TOKEN_ENDPOINT")
				.type(STRING_TYPE).label("OAuth2 Token Endpoint")
				.defaultValue("http://localhost/auth/realms/XXXXX/protocol/openid-connect/token")
				.helpText("Endpoint to negotiate the token with client_credentials grant type (required for OAUTH authorization)")
				.add();
		builder.property().name("PROPERTY_OAUTH_CLIENT_ID")
				.type(STRING_TYPE).label("OAuth2 Client Id")
				.defaultValue("")
				.helpText("client_id to negotiate the Access Token (required for OAUTH authorization)")
				.add();
		builder.property().name("PROPERTY_OAUTH_CLIENT_SECRET")
				.type(PASSWORD).label("OAuth2 Client Secret")
				.defaultValue("")
				.helpText("client_secret to negotiate the Access Token (required for OAUTH authorization)")
				.add();
		builder.property().name("PROPERTY_OAUTH_SCOPE")
				.type(STRING_TYPE).label("OAuth2 Scope")
				.defaultValue("")
				.helpText("Required scope in the access_token request")
				.add();
		builder.property().name("PROPERTY_BASIC_USERNAME")
				.type(STRING_TYPE).label("Auth Basic Username")
				.defaultValue("")
				.helpText("Username used for Basic Authentication")
				.add();
		builder.property().name("PROPERTY_BASIC_PASSWORD")
				.type(PASSWORD).label("Auth Basic Password")
				.defaultValue("")
				.helpText("Password used for Basic Authentication")
				.add();
		builder.property().name("CREATE_USERS_LOCALLY")
				.type(BOOLEAN_TYPE).label("Create users locally")
				.defaultValue(false)
				.helpText("Enable to stop creating users remotly and start creating them locally.")
				.add();
		configMetadata = builder.build();
	}

	@Override
	public KeycloakRestRepoProvider create(KeycloakSession session, ComponentModel model) {
		logger.infov("Creating a new instance of restHandler");
		RestConfiguration restConfiguration = new RestConfiguration(model.getConfig());
		restHandler = new RestHandler(restConfiguration);
		return new KeycloakRestRepoProvider(session, model, restHandler);
	}

	@Override
	public String getId() {
		return "rest-repo-provider";
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return configMetadata;
	}

	@Override
	public void close() {
		this.httpStats.entrySet().stream().forEach(a -> a.getValue().cancel());
	}
}
