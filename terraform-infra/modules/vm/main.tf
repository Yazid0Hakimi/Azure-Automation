# dns resolution for AKS cluster
resource "azurerm_private_dns_zone" "private_dns" {
  name                = "privatelink.${var.location}.azmk8s.io"
  resource_group_name = var.resource_group_name
}

resource "azurerm_private_dns_zone_virtual_network_link" "private_dns_link" {
  name                  = "link-to-vnet"
  resource_group_name   = var.resource_group_name
  private_dns_zone_name = azurerm_private_dns_zone.private_dns.name
  virtual_network_id    = var.vnet_id
}


  #---------
resource "azurerm_public_ip" "admin_vm_pip" {
  name                = "admin-vm-pip"
  location            = var.location
  resource_group_name = var.resource_group_name
  allocation_method   = "Dynamic"
  sku                 = "Basic"
}

resource "azurerm_network_interface" "main" {
  name                = "admin-vm-nic"
  location            = var.location
  resource_group_name = var.resource_group_name

  ip_configuration {
    name                          = "testconfiguration1"
    subnet_id                     = var.subnet_id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.admin_vm_pip.id
  }
}

resource "azurerm_linux_virtual_machine" "main" {
  name                  = var.vm_name
  location              = var.location
  resource_group_name   = var.resource_group_name
  network_interface_ids = [azurerm_network_interface.main.id]
  size                = "Standard_F2"
  admin_username      = "adminuser"
custom_data = base64encode(file("modules/vm/cloud-init.yaml"))
 
  admin_ssh_key {
    username   = "adminuser"
    public_key = var.ssh_public_key 
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }
  tags = {
    environment = "dev"
  }
}

output "admin_vm_id" {
  value       = azurerm_linux_virtual_machine.main.id
  description = "The ID of the admin VM"
}

output "admin_vm_public_ip" {
  value       = azurerm_public_ip.admin_vm_pip.ip_address
  description = "The public IP address of the admin VM"
}

output "admin_vm_private_ip" {
  value       = azurerm_network_interface.main.private_ip_address
  description = "The private IP address of the admin VM"
}

output "admin_vm_name" {
  value       = azurerm_linux_virtual_machine.main
  description = "The name of the admin VM"
}
output "private_dns_zone_id" {
  value       = azurerm_private_dns_zone.private_dns.id
  description = "The ID of the private DNS zone for AKS"
}