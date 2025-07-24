variable "vnet_name" {
  description = "The name of the VNet"
  type        = string
}

variable "address_space" {
  description = "Address space for the VNet"
  type        = string
}

variable "location" {
  description = "The Azure location where the VNet will be created"
  type        = string
}

variable "resource_group" {
  description = "The name of the resource group"
  type        = string
}

variable "vnet_type" {
  description = "Type of VNet, can be 'hub' or 'spoke'"
  type        = string
}
