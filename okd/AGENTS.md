# AGENTS.md

## Repository

Ansible automation for O-RAN O-Cloud on OKD under `pti-rtp/okd`. Orchestration entry: `playbooks/ocloud.yml` → role `ocloud`.

## Documentation

- **[AGENTS.md](./AGENTS.md)** — Ansible expertise, project patterns, collections, conventions, and OKD/O-RAN context
- **[docs/OKD-DOCS-REFERENCE.md](./docs/OKD-DOCS-REFERENCE.md)** — Versioned OKD documentation links and structure

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

---

## ANSIBLE EXPERTISE

### Playbooks & Roles

- Structure roles with full `defaults/`, `vars/`, `tasks/`, `handlers/`,
  `templates/`, `files/`, `meta/` layout
- Use `defaults/main.yml` for user-overridable variables; `vars/main.yml` for
  role-internal constants
- Role dependencies declared in `meta/main.yml`; avoid circular dependencies
- Always define `meta/main.yml` with `min_ansible_version`, `platforms`, and
  `galaxy_info`
- Use `include_role` and `include_tasks` for dynamic inclusion (loops,
  conditionals)
- Use `import_role` and `import_tasks` for static role inclusion in plays
  (parsed at compile time)
- Block/rescue/always patterns for error handling and guaranteed cleanup
- Notify handlers by name; use `flush_handlers` deliberately when intermediate
  state matters
- Use `listen` on handlers to allow multiple tasks to trigger the same handler
- `pre_tasks` and `post_tasks` for bootstrapping and validation steps
- Avoid `ignore_errors: true` broadly — scope it tightly and log when used
- Avoid hardcoded paths, users, or IPs inside roles — always parameterize via
  defaults

### Inventory & Variables

- Group hierarchy: `all > host_group > host`
- `group_vars/all/` for org-wide defaults; `group_vars/<group>/` for scoped
  overrides
- `host_vars/<hostname>/` only for host-specific overrides that cannot be
  grouped
- Variable precedence (low to high): role defaults → inventory → group_vars →
  host_vars → play vars → extra_vars
- Use `ansible-inventory --list` and `--graph` to debug inventory resolution
- Use `set_fact` sparingly — prefer passing variables cleanly through inventory
  and role defaults
- `magic variables`: leverage `hostvars`, `groups`, `inventory_hostname`,
  `ansible_facts` fluently
- Use `ansible_facts` gathered facts for OS-conditional logic rather than
  hardcoding distro checks
- Gather facts selectively (`gather_subset`) to speed up runs on large
  inventories

### Vault & Secrets

- Never store plaintext secrets in any file committed to version control — ever
- Use `ansible-vault encrypt_string` for inline secrets in var files; full-file
  encryption for dense secret files
- Use `no_log: true` on tasks that handle secrets; audit all places secrets
  might appear in output
- Avoid passing secrets as extra-vars on the CLI (`-e`) — they appear in process
  lists and logs

### Performance & Scaling

- SSH multiplexing: `ControlMaster=auto`, `ControlPersist=60s`, `ControlPath` in
  `ansible.cfg` or SSH config
- Use `pipelining = true` in `ansible.cfg` to reduce SSH round-trips (requires
  `requiretty` disabled in sudoers)
- Use `async` and `poll` for long-running tasks (package installs, large file
  transfers) to parallelize across hosts
- Profile slow playbooks with `ANSIBLE_CALLBACK_WHITELIST=profile_tasks`
- Delegate tasks to localhost or a jump host with `delegate_to` to reduce
  target-side overhead
- Avoid `gather_facts: true` globally on plays that don't need facts — use
  `gather_facts: false` and gather selectively

---

## COLLECTIONS & MODULE STYLE

### Rules for agents

1. **FQCN always** — `ansible.builtin.git`, `kubernetes.core.k8s`; never short
   names (`git`, `k8s`).
2. **`okd/requirements.yml` is authoritative** — install before running
   playbooks; do not add modules from collections not listed there.
3. **Pick the collection by task type** — use the list below; do not substitute
   a "similar" module from another collection.


### Collections

- `ansible.builtin`
- `kubernetes.core`
- `community.general`
- `community.libvirt`
- `ansible.posix`
- `ansible.utils`
- `containers.podman`
- `community.crypto`

---

## OKD DOCUMENTATION REFERENCE

@OKD-DOCS-REFERENCE.md — See [docs/OKD-DOCS-REFERENCE.md](./docs/OKD-DOCS-REFERENCE.md) for:
- Versioned OKD documentation links (4.21, 4.22, latest)
- Documentation structure across all versions
- Quick links to installation, networking, storage, edge/telco, operators
- Current target: OKD 4.22 / CentOS Stream CoreOS (SCOS)

**Quick links for current target (4.22):**
- Installation overview: https://docs.okd.io/4.22/installing/overview/index.html
- Bare metal IPI: https://docs.okd.io/4.22/installing/installing_bare_metal_ipi/ipi-install-overview.html
- SNO (Single-Node): https://docs.okd.io/4.22/installing/installing_sno/install-sno-installing-sno.html
- Edge computing: https://docs.okd.io/4.22/edge_computing/index.html
- SR-IOV: https://docs.okd.io/4.22/networking/hardware_networks/about-sriov.html

For comprehensive documentation references across all versions, see docs/OKD-DOCS-REFERENCE.md.

---

## SOURCE REPOSITORY: O-RAN SC pti-rtp/okd

Repository: https://gerrit.o-ran-sc.org/r/admin/repos/pti/rtp (okd/
subdirectory)
Cloned repository: https://github.com/o-ran-sc/pti-rtp/tree/master/okd
License: Apache 2.0

### Project Context

- This directory contains Ansible-based deployment automation for OKD O-Cloud
  clusters as part of the O-RAN Software Community (O-RAN SC) Infrastructure
  (INF) project.
- OKD is the upstream open-source distribution of Red Hat OpenShift Container
  Platform.
- The `okd/` tree provides end-to-end automation for deploying OKD (currently
  targeting OKD 4.22 / CentOS Stream CoreOS) as a compliant O-RAN O-Cloud
  platform on bare metal and VMs.
- OKD is a subproject of the O-RAN-SC Performance Tuned Infrastructure (pti)
  Real Time Platform (rtp).

The O-Cloud model supports two primary topologies:

- Single-node OKD (SNO): all-in-one control plane + worker + storage on a single
  physical host.
- Multi-node OKD: minimum 3 control plane nodes, scalable up to ~2,000 worker
  nodes.

### What the okd/ Automation Does

- Full OKD cluster lifecycle: install, configure, validate, and upgrade.
- Integrates the following operators into the OKD O-Cloud:
  - Stolostron (ACM / multicluster hub).
  - The `oran-o2ims` operator exposes the O-RAN O2 IMS/DMS interface to the SMO.
  - The `multi-cluster-observability` operator.
  - The `cluster-group-upgrades` (TALM) operator.
- Supporting playbooks/roles for O2 compliance testing automation.
- Sample workload deployment for validation.

### Key Ansible Roles (okd/roles/)

Orchestration entry: **`ocloud`** imports the roles below (see
`roles/ocloud/tasks/main.yml`).
Names match directory names under `roles/`.

- **ocloud_setup**: Performs initial environment setup, handles creation of
  staging directories, fetches and prepares prerequisites for the O-Cloud
  install process.
- **ocloud_platform_okd**: Automates the full installation of the OKD cluster,
  including generating install configs, managing cluster creation with the
  installer, and gathering kubeconfig/secrets.
- **ocloud_platform_stolostron**: Installs and configures Advanced Cluster
  Management (Stolostron) to enable multi-cluster management and observability,
  supporting O-RAN O-Cloud compliance.
- **ocloud_platform_o2ims**: Installs and sets up the O-RAN O2 IMS/DMS operator,
  exposing necessary O2 interfaces for integration with Service Management and
  Orchestration (SMO) platforms.
- **ocloud_platform_mco**: Deploys the Multi-Cluster Observability Operator to
  provide metrics and monitoring across managed clusters.
- **ocloud_platform_cgu**: Installs the TALM/cluster-group-upgrades operator,
  facilitating automated and orchestrated upgrades of OKD clusters as required
  for O-Cloud lifecycle.

- These roles can be invoked individually or as part of end-to-end playbooks,
  depending on your deployment and customization needs.
- See playbooks under `okd/playbooks/` for usage examples and orchestration
  patterns.

---

### Key Variables & Patterns

- `ocloud_staging_dir['path']` — tempfile-registered staging dir
  (`roles/ocloud_setup`); Git clones under `git/`, OKD installer under `bin/`
  and `cfg/`
- `ocloud_kubeconfig` — KUBECONFIG path; set after OKD install
  (`cfg/auth/kubeconfig`) or passed via `-e` for post-install roles; always
  passed explicitly to `kubernetes.core.*` and shell `environment:`
- `ocloud_platform_stolostron_repo_url` / `ocloud_platform_stolostron_snapshot`
  — stolostron/deploy clone URL and pinned snapshot (`snapshot.ver`); install
  via `./start.sh`, not `make`
- `ocloud_platform_o2ims_repo_url` / `ocloud_platform_o2ims_repo_version` —
  oran-o2ims clone; `make install deploy` in `roles/ocloud_platform_o2ims`
- `ocloud_platform_okd_release` / `ocloud_platform_okd_pull_secret` /
  `ocloud_platform_okd_ssh_pubkey` — OKD install inputs
  (`roles/ocloud_platform_okd/defaults/main.yml`)
- Siteconfig is not a separate Ansible role: enabled via
  `kubernetes.core.k8s_json_patch` on `MultiClusterHub`
  (`roles/ocloud_platform_stolostron/tasks/main.yml`)
- `ansible_env.PATH` is extended per task: `.../bin` for OKD/stolostron tooling;
  `.../go/bin` for Go operator builds (o2ims, cgu, hwmgr)
- Manifests and ClusterImageSets (e.g.,
  `manifests-examples/clusterimagesets/4.22.0-okd-scos.1.yaml`) live under
  `okd/manifests-examples/`

### Conventions & Patterns Observed in This Repo

- Tasks use FQCN (`ansible.builtin.git`, `kubernetes.core.k8s_json_patch`) —
  never short-form
- Shell tasks always include `chdir:` and explicit `environment:` — never rely
  on ambient shell state
- Operator installs are staged: clone → build/install → patch CR to enable —
  never in a single task
- KUBECONFIG is always passed explicitly; never relies on default
  `~/.kube/config`
- `kubernetes.core.k8s_json_patch` used for targeted CR modifications (e.g.,
  enabling a component in MultiClusterHub); `kubernetes.core.k8s` used for full
  resource creates/applies
- O2 interface compliance is validated via separate dedicated playbooks/roles,
  not inline in install plays

### Working With This Repo

- Clone: `git clone https://github.com/o-ran-sc/pti-rtp.git && cd pti-rtp/okd`
- See `okd/README.md` for full prerequisites and deployment steps.
- Requires: Python 3, Ansible >= 2.15, collections from `requirements.yml` (see
  COLLECTIONS & MODULE STYLE), `kubectl`/`oc` on PATH, valid KUBECONFIG.
- For operator installs: Go toolchain must be available at
  `{{ ocloud_staging_dir['path'] }}/go/bin`.
- Upstream patches/reviews go through Gerrit:
  https://gerrit.o-ran-sc.org/r/admin/repos/pti/rtp

---
