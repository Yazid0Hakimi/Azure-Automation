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
variable "vnet_id" {
  type = string
}
variable "ssh_public_key" {
  description = "SSH public key for VM authentication"
  type        = string
  sensitive   = true
}
