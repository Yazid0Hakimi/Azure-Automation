resource "azurerm_virtual_network" "vnet" {
  name                = var.vnet_name
  location            = var.location
  resource_group_name = var.resource_group
  address_space       = [var.address_space]

  tags = {
    environment = "dev"
    vnet_type   = var.vnet_type # Add vnet_type to tags for identification
  }
}

output "vnet_name" {
  value = azurerm_virtual_network.vnet.name
}

output "vnet_id" {
  value = azurerm_virtual_network.vnet.id
}
