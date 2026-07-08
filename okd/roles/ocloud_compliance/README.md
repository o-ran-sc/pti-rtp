# ocloud_compliance Role

Automates execution of O-RAN O2 IMS compliance tests from the [it/test](https://gerrit.o-ran-sc.org/r/q/project:it/test) repository.

## Requirements

- Python 3
- Ansible >= 2.15
- Access to target O-Cloud cluster with valid KUBECONFIG
- Ansible Vault encrypted SSH credentials

## Role Variables

### Required Variables

These variables **MUST** be provided and have no defaults:

| Variable | Description | Example | Security |
|----------|-------------|---------|----------|
| `ocloud_compliance_ssh_user` | SSH username for compliance test access | `compliance-tester` | **MUST be vault-encrypted** |
| `ocloud_compliance_ssh_password` | SSH password for compliance test access | `SecureP@ssw0rd!` | **MUST be vault-encrypted** |

### Optional Variables

See `defaults/main.yml` for full list.

## Setting Up SSH Credentials (REQUIRED)

### Step 1: Navigate to Your Inventory

```bash
cd /path/to/okd/inventory
```

### Step 2: Create or Edit Vault File

Create a new vault file:
```bash
ansible-vault create group_vars/all/vault-compliance.yml
```

Or edit existing vault:
```bash
ansible-vault edit group_vars/all/vault.yml
```

### Step 3: Generate Encrypted Credentials

**Outside the vault editor**, generate encrypted values:

```bash
# Encrypt username
ansible-vault encrypt_string 'your-username' --name 'ocloud_compliance_ssh_user'

# Encrypt password  
ansible-vault encrypt_string 'your-password' --name 'ocloud_compliance_ssh_password'
```

### Step 4: Add to Vault File

Copy and paste the output from Step 3 into your vault file:

```yaml
ocloud_compliance_ssh_user: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          66386439653237336366383433373935623462336432323937663966653566356166363635353364
          # ... (full encrypted string)

ocloud_compliance_ssh_password: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          33636134363630363164626536396166353533373631363739393766353661393634356365393537
          # ... (full encrypted string)
```

### Step 5: Save and Exit

Save the vault file (it will be encrypted).

## Running the Playbook

```bash
ansible-playbook -i inventory playbooks/ocloud_compliance.yml \
  --ask-vault-pass \
  -e ocloud_kubeconfig=$KUBECONFIG \
  -e ocloud_compliance_resource_type="HPE/ProLiant DL360 Gen10 Plus (P28948-B21)" \
  -e ocloud_compliance_resource_desc_substring="DL360"
```

## Security Notes

- **NEVER** commit plaintext credentials to version control
- **ALWAYS** use `ansible-vault` for sensitive variables
- Use `--ask-vault-pass` or `--vault-password-file` when running playbooks
- Restrict vault file permissions: `chmod 600 group_vars/all/vault*.yml`
- Rotate credentials regularly

## Troubleshooting

### "ocloud_compliance_ssh_user must be set"

You haven't configured SSH credentials in your vault file. See "Setting Up SSH Credentials" above.

### "Default credentials detected"

You're using the old insecure `robot:robot` credentials. These are no longer allowed. Generate new vault-encrypted credentials.

## License

Apache 2.0

## Author

O-RAN SC Infrastructure (INF) project
