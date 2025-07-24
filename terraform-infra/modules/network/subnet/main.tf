resource "azurerm_subnet" "subnet" {
  name                 = var.subnet_name
  resource_group_name  = var.resource_group_name
  virtual_network_name = var.vnet_name
  address_prefixes     = [var.address_prefix]
  service_endpoints    = ["Microsoft.KeyVault"]
}
output "subnet_id" {
  value = azurerm_subnet.subnet.id
}
