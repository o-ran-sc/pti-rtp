# ocloud_compliance Role

Automates execution of O-RAN O2 IMS compliance tests from the [it/test](https://gerrit.o-ran-sc.org/r/q/project:it/test) repository.

## Requirements

- Python 3
- Ansible >= 2.15
- Access to target O-Cloud cluster with valid KUBECONFIG
- Ansible Vault encrypted SSH credentials

## Role Variables

See `defaults/main.yml` for full list.

## Setting Up SSH Credentials (REQUIRED)

### Step 1: Navigate to Your Inventory

```bash
cd /path/to/okd/inventory
```

### Step 2: Create or Edit Vault File

Create a new encrypted vault file (opens your editor):

```bash
ansible-vault create group_vars/all/vault.yml
```

Or edit an existing encrypted vault:

```bash
ansible-vault edit group_vars/all/vault.yml
```

You will be prompted for a vault password. Use a strong password and store it securely.

### Step 3: Add Credentials to Vault

Inside the vault editor, add your SSH credentials as plain YAML:

```yaml
---
# SSH credentials for O2 compliance testing
ocloud_compliance_ssh_user: your-actual-username
ocloud_compliance_ssh_password: your-actual-password
```

Replace `your-actual-username` and `your-actual-password` with your real credentials.

### Step 4: Save and Exit

Save the file and exit your editor. The entire vault file will be automatically encrypted when you save.

### Step 5: Verify Encryption

Verify the vault file is encrypted:

```bash
# This should show encrypted content:
cat group_vars/all/vault.yml

# This should show decrypted content (requires vault password):
ansible-vault view group_vars/all/vault.yml
```

**Note:** The entire vault file is encrypted using `ansible-vault encrypt`. You can add multiple secrets to the same vault file.

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
