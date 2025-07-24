# # Add these outputs to your existing outputs.tf file or create a new one
# output "admin_vm_public_ip" {
#   value       = module.vm.admin_vm_public_ip
#   description = "The public IP address of the admin VM"
# }

# output "admin_vm_ssh_command" {
#   value       = "ssh -i ~/.ssh/id_rsa ${var.admin_username}@${module.vm.admin_vm_public_ip}"
#   description = "Command to SSH into the admin VM"
#   sensitive   = false
# }