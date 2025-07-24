variable "location" {
  type = string
}

variable "resource_group_name" {
  type = string
}

variable "subnet_id" {
  type = string
}
variable "vm_name" {
  type = string
}

variable "admin_username" {
  type = string
}


variable "tenant_id" {
  description = "tenant id"
  type        = string
}

variable "keyvault_name" {
  description = "key vault name"
  type        = string
}

variable "keyvault_id" {
  description = "key vault name"
  type        = string
}
variable "public_ip_id" {
  description = "public ip id"
  type        = string
}
variable "vnet_id" {
  type = string
}