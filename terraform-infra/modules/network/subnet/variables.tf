variable "location" {
  description = "The Azure location where the resources will be created."
  type        = string
}

variable "resource_group_name" {
  description = "The name of the resource group where the subnet will be created."
  type        = string
}

variable "vnet_id" {
  description = "The ID of the Virtual Network to which the subnet will be attached."
  type        = string
}

variable "vnet_name" {
  description = "The name of the subnet to be created."
  type        = string
}

variable "subnet_name" {
  description = "The name of the subnet to be created."
  type        = string
}

variable "address_prefix" {
  description = "The address prefix for the subnet."
  type        = string
}


