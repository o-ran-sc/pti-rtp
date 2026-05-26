# CLAUDE.md

@AGENTS.md — also see [AGENTS.md](./AGENTS.md) for project context (roles, variables, FQCN, OKD/O-RAN). Do not duplicate it here.

## Repository

Ansible automation for O-RAN O-Cloud on OKD under `pti-rtp/okd`. Orchestration entry: `playbooks/ocloud.yml` → role `ocloud`.

## Quick commands

Run from `okd/`:

```bash
ansible-galaxy collection install -r requirements.yml
pip install -r requirements.txt
ansible-playbook --syntax-check playbooks/ocloud.yml -i inventory-examples/ocloud-vm-okd-aio
ansible-lint playbooks/ocloud.yml
yamllint roles/
```

Deploy (after inventory under `inventory/` or copied from `inventory-examples/`):

```bash
ansible-playbook -i inventory playbooks/ocloud.yml
```

Full prerequisites and post-install flows: [README.md](./README.md).

---

## ANSIBLE GENERAL PHILOSOPHY

- Always write idempotent tasks; check before change, never assume state
- Prefer declarative modules over shell/command unless no module exists
- Use `ansible-lint` and `yamllint` as part of every development workflow
- Follow the principle of least privilege — limit what each play and role can do
- Pin collection and role versions in production; test upgrades in staging first
- Tag everything meaningfully: `--tags` and `--skip-tags` are production tools

---

## RESPONSE STYLE & DEFAULTS

- Default to RHEL 8/9 syntax and behaviors unless told otherwise
- Provide full working YAML examples, not pseudocode
- Always include `name:` on every task — no anonymous tasks
- Quote strings in YAML that contain special characters; use block scalars for multi-line values
- Call out idempotency concerns explicitly when a pattern has risks
- When suggesting shell/command, note what module should be used instead if one exists
- Flag deprecated syntax or modules and suggest the current replacement
- Assume production-grade context: suggest `--check` mode, change validation, and rollback considerations
- When reviewing playbooks, check for: hardcoded values, missing `no_log`, non-idempotent tasks, missing tags, and missing handlers
- Be direct and opinionated — recommend best practices, not just options
