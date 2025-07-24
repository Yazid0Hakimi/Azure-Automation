data "azurerm_client_config" "current" {}

locals {
  sub       = data.azurerm_client_config.current.subscription_id
  tenant_id = data.azurerm_client_config.current.tenant_id
  # tenant id : 84f1e4ea-8554-43e1-8709-f0b8589ea118 az account show --query tenantId --output tsv

  client_id           = data.azurerm_client_config.current.client_id
  object_id           = data.azurerm_client_config.current.object_id
  location            = "southcentralus"
  resource_group_name = "1-912eadbf-playground-sandbox"
}

