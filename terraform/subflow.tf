resource "keycloak_authentication_subflow" "mi-subflow" {
  for_each          = toset([keycloak_authentication_flow.flow1.alias, keycloak_authentication_flow.flow2.alias])
  realm_id          = keycloak_realm.realm.id
  alias             = "my-subflow-${each.key}"
  parent_flow_alias = each.key
  provider_id       = "basic-flow"
  requirement       = "REQUIRED"
  description       = "My subflow ${each.key}"
}

resource "keycloak_authentication_execution" "execution_one" {
  for_each          = toset([keycloak_authentication_subflow.mi-subflow["flow-1"].alias, keycloak_authentication_subflow.mi-subflow["flow-2"].alias])
  realm_id          = keycloak_realm.realm.id
  parent_flow_alias = each.key
  authenticator     = "auth-cookie"
  requirement       = "ALTERNATIVE"
}

resource "keycloak_authentication_execution" "execution_two" {
  for_each          = toset([keycloak_authentication_subflow.mi-subflow["flow-1"].alias, keycloak_authentication_subflow.mi-subflow["flow-2"].alias])
  realm_id          = keycloak_realm.realm.id
  parent_flow_alias = each.key
  authenticator     = "identity-provider-redirector"
  requirement       = "ALTERNATIVE"

  depends_on = [
    keycloak_authentication_execution.execution_one
  ]
}