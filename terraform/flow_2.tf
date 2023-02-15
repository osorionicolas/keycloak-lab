resource "keycloak_authentication_flow" "flow2" {
  realm_id = keycloak_realm.realm.id
  alias    = "flow-2"
  description = "Este es el flow numero 2"
}