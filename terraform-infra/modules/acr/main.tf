resource "random_string" "unique_name" {
  length  = 6
  special = false
}

resource "azurerm_container_registry" "main" {
  name                   = "aksdemo${random_string.unique_name.result}"
  resource_group_name    = var.resource_group_name
  location               = var.location
  sku                    = var.acr_sku
  admin_enabled          = var.acr_admin_enabled
  anonymous_pull_enabled = var.acr_anonymous_pull_enabled
}

output "acr_id" {
  value = azurerm_container_registry.main.id
}