# OKD O-Cloud Test Suite

This directory contains automated tests for the OKD O-Cloud Ansible automation.

## Security Tests

### test-compliance-security.sh

**Purpose:** Validates TECE-497 security fix for hardcoded credentials in `ocloud_compliance` role

**What it tests:**
1. Missing credentials are rejected with clear error message
2. Default `robot:robot` credentials are rejected
3. Plaintext credentials trigger warnings
4. Defaults file has credentials set to `null` (not hardcoded)
5. Sensitive tasks have `no_log: true` to prevent credential leakage

**Usage:**

```bash
# Run from okd/ directory
./tests/test-compliance-security.sh
```

**Expected behavior:**

- **Before TECE-497 fix:** Tests show warnings indicating hardcoded credentials exist
- **After TECE-497 fix:** All validation checks should trigger and pass

**Exit codes:**
- `0` - All tests passed
- `1` - Some tests failed (expected before fix is applied)

**Output:**
- Green ✓ - Test passed
- Yellow ⚠ - Warning (fix not yet applied, but test structure is valid)
- Red ✗ - Test failed unexpectedly

## Adding New Tests

To add new security or functional tests:

1. Create a new test script in this directory (e.g., `test-operator-security.sh`)
2. Make it executable: `chmod +x tests/test-*.sh`
3. Follow the same structure:
   - Set up test environment
   - Run test cases with clear pass/fail criteria
   - Clean up test artifacts
   - Print colored summary
4. Update this README with test documentation

## Integration Tests

For full integration testing of the compliance playbook:

```bash
# 1. Create vault-encrypted credentials
ansible-vault create inventory/group_vars/all/vault-compliance.yml

# Add these variables (encrypted):
# ocloud_compliance_ssh_user: your-username
# ocloud_compliance_ssh_password: your-password

# 2. Run full compliance playbook
ansible-playbook -i inventory playbooks/ocloud_compliance.yml \
  --ask-vault-pass \
  -e ocloud_kubeconfig=$KUBECONFIG \
  -e ocloud_compliance_resource_type="HPE/ProLiant DL360 Gen10 Plus (P28948-B21)" \
  -e ocloud_compliance_resource_desc_substring="DL360"
```

## CI/CD Integration

To run these tests in CI/CD pipelines:

```yaml
# Example GitHub Actions / GitLab CI
- name: Run security tests
  run: |
    cd okd/
    ./tests/test-compliance-security.sh
```

## Troubleshooting

### Test fails with "playbook not found"
- Ensure you're running from `okd/` directory: `cd /path/to/okd && ./tests/test-compliance-security.sh`

### Test creates leftover files
- The test uses `trap cleanup EXIT` to auto-cleanup
- If interrupted, manually remove: `rm -rf test-inventory-compliance/`

### Syntax check fails
- Run `ansible-playbook --syntax-check playbooks/ocloud_compliance.yml` separately
- Check for YAML syntax errors in the playbook or roles
