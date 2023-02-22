package com.identicum.keycloak;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.services.messages.Messages;

import javax.ws.rs.core.Response;
public class PasswordFormAuthenticator extends UsernamePasswordForm {
	

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		// Generar codigo random y mostrarlo en el theme
		LoginFormsProvider form = context.form();

		this.displayRandom(form);

		super.authenticate(context);
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		// Obtener informacion de la request y validar si es igual al codigo generado
		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

		String username = formData.getFirst("username");
		if(username.isBlank()){
			Response failureChallenge = challenge(context, Messages.INVALID_USERNAME, null);
			context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, failureChallenge);
			super.action(context);
			return;
		}
		
		super.action(context);
		return;
	}

	private void displayRandom(LoginFormsProvider form) {
		// Generar codigo random y mostrarlo en el theme
		Double random = Math.random();
		String display = random.toString();
		form.setAttribute("random", display);
	}

}
