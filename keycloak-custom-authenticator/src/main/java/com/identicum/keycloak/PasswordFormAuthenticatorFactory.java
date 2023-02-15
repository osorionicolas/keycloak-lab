package com.identicum.keycloak;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.credential.WebAuthnCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class PasswordFormAuthenticatorFactory implements AuthenticatorFactory {

	public static final String PROVIDER_ID = "password-form-authenticator";
	private static final PasswordFormAuthenticator SINGLETON = new PasswordFormAuthenticator();

	private static Map<String, Timer> httpStats = new HashMap<String, Timer>();

	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	@Override
	public String getDisplayType() {
		return "Password Form Authenticator";
	}

	@Override
	public String getReferenceCategory() {
		return WebAuthnCredentialModel.TYPE_PASSWORDLESS;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	@Override
	public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
		return Arrays.asList(AuthenticationExecutionModel.Requirement.REQUIRED).toArray(new AuthenticationExecutionModel.Requirement[0]);
	}

	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}

	@Override
	public String getHelpText() {
		return null;
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}

	@Override
	public void init(Config.Scope scope) {

	}

	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

	}

	@Override
	public void close() {
		httpStats.entrySet().stream().forEach(a -> a.getValue().cancel());
	}

	@Override
	public Authenticator create(KeycloakSession session) {
		return SINGLETON;
	}
}
