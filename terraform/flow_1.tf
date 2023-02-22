resource "keycloak_authentication_flow" "flow1" {
  realm_id = keycloak_realm.realm.id
  alias    = "flow-1"
  description = "Este es el flow numero 1"
}