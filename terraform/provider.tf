terraform {
  required_providers {
    keycloak = {
      source  = "mrparkers/keycloak"
      version = "= 4.0.1"
    }
  }
}

provider "keycloak" {
    client_id     = "admin-cli"
    username      = "admin"
    password      = "admin"
    url           = "http://localhost:8080"
    base_path     = "/auth"
}