# AGENTS.md

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

```bash
ansible-galaxy collection install -r requirements.yml
```

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

Primary docs: https://docs.okd.io/
Use https://docs.okd.io/latest/ for the current release at any time.

### Versioned References

- Version 4.19: Target version in pti-rtp okd automation (OKD 4.19 / SCOS).
  https://docs.okd.io/4.19/welcome/index.html
- Version 4.20: Current stable release.
  https://docs.okd.io/4.20/welcome/index.html
- Version 4.21: Latest available release.
  https://docs.okd.io/4.21/welcome/index.html

### Documentation Structure (consistent across all versions)

Each version organizes its docs into the following top-level sections.
When answering questions, link to the specific version requested or default to
`latest`.
Replace `{version}` in URLs below with e.g. `4.19`, `4.20`, or `latest`.

#### Overview / Welcome

- Product introduction and Kubernetes overview
- OpenShift editions comparison
- Welcome index.
  https://docs.okd.io/{version}/welcome/index.html

#### What's New

- New features and enhancements per release
- Deprecated features (check before upgrading automation)
- New features.
  https://docs.okd.io/{version}/whats_new/new-features.html
- Deprecated features.
  https://docs.okd.io/{version}/whats_new/deprecated-features.html

#### Architecture

- Product architecture overview.
  https://docs.okd.io/{version}/architecture/architecture.html
- Installation and update architecture.
  https://docs.okd.io/{version}/architecture/architecture-installation.html
- Control plane architecture.
  https://docs.okd.io/{version}/architecture/control-plane.html
- Fedora CoreOS (FCOS) / CentOS Stream CoreOS (SCOS).
  https://docs.okd.io/{version}/architecture/architecture-rhcos.html
- Admission plugins.
  https://docs.okd.io/{version}/architecture/admission-plug-ins.html

#### Disconnected Environments

Critical for O-RAN / telecom deployments.

- About disconnected environments.
  https://docs.okd.io/{version}/disconnected/about.html
- oc-mirror plugin v2 (preferred).
  https://docs.okd.io/{version}/disconnected/about-installing-oc-mirror-v2.html
- Migrating oc-mirror v1 to v2.
  https://docs.okd.io/{version}/disconnected/oc-mirror-migration-v1-to-v2.html
- Installing in disconnected environment.
  https://docs.okd.io/{version}/disconnected/installing.html
- OLM in disconnected environments.
  https://docs.okd.io/{version}/disconnected/using-olm.html
- Disconnected cluster updates.
  https://docs.okd.io/{version}/disconnected/updating/disconnected-update.html
- Mirror registry setup.
  https://docs.okd.io/{version}/disconnected/installing-mirroring-creating-
  registry.html

#### Installing

- Installation overview.
  https://docs.okd.io/{version}/installing/overview/index.html
- Cluster capabilities (optional components).
  https://docs.okd.io/{version}/installing/overview/cluster-capabilities.html
- Bare metal IPI.
  https://docs.okd.io/{version}/installing/installing_bare_metal_ipi/ipi-
  install-overview.html
- Bare metal UPI.
  https://docs.okd.io/{version}/installing/installing_bare_metal/installing-
  bare-metal.html
- SNO (Single-Node OpenShift).
  https://docs.okd.io/{version}/installing/installing_sno/install-sno-
  installing-sno.html
- Platform-agnostic (agent-based).
  https://docs.okd.io/{version}/installing/installing_with_agent_based_installer
  /preparing-to-install-with-agent-based-installer.html
- AWS IPI.
  https://docs.okd.io/{version}/installing/installing_aws/ipi/installing-aws-
  default.html
- Azure IPI.
  https://docs.okd.io/{version}/installing/installing_azure/ipi/installing-
  azure-default.html
- vSphere IPI.
  https://docs.okd.io/{version}/installing/installing_vsphere/ipi/ipi-vsphere-
  installation-reqs.html
- On-premises (bare metal) is the relevant path for O-RAN SC INF/OKD deployments

#### Post-installation Configuration

- Cluster tasks.
  https://docs.okd.io/{version}/post_installation_configuration/cluster-
  tasks.html
- Machine config operator (MCO).
  https://docs.okd.io/{version}/post_installation_configuration/machine-
  configuration-tasks.html
- Node tuning (performance-addon/tuned).
  https://docs.okd.io/{version}/post_installation_configuration/node-tasks.html

#### Updating Clusters

- Update overview.
  https://docs.okd.io/{version}/updating/understanding_updates/intro-to-
  updates.html
- CLI updates.
  https://docs.okd.io/{version}/updating/updating_a_cluster/updating-cluster-
  cli.html
- TALM (cluster-group-upgrades).
  https://docs.okd.io/{version}/edge_computing/cnf-talm-for-cluster-updates.html

#### Operators / OLM

- OLM overview.
  https://docs.okd.io/{version}/operators/understanding/olm/olm-understanding-
  olm.html
- OperatorHub.
  https://docs.okd.io/{version}/operators/admin/olm-adding-operators-to-
  cluster.html
- Operator SDK.
  https://docs.okd.io/{version}/operators/operator_sdk/osdk-about.html

#### Networking

- Networking overview.
  https://docs.okd.io/{version}/networking/understanding-networking.html
- OVN-Kubernetes.
  https://docs.okd.io/{version}/networking/ovn_kubernetes_network_provider/about
  -ovn-kubernetes.html
- SR-IOV (critical for O-RAN O-DU).
  https://docs.okd.io/{version}/networking/hardware_networks/about-sriov.html
- MetalLB.
  https://docs.okd.io/{version}/networking/metallb/about-metallb.html

#### Storage

- Storage overview.
  https://docs.okd.io/{version}/storage/index.html
- Persistent volumes.
  https://docs.okd.io/{version}/storage/understanding-persistent-storage.html
- CSI drivers.
  https://docs.okd.io/{version}/storage/container_storage_interface/persistent-
  storage-csi.html

#### Edge / Telco / RAN Specific

- Edge computing overview.
  https://docs.okd.io/{version}/edge_computing/index.html
- RAN DU reference design.
  https://docs.okd.io/{version}/edge_computing/ran_du_deployment_guide/ztp-ran-
  du-deployment-overview.html
- ZTP (Zero Touch Provisioning / GitOps).
  https://docs.okd.io/{version}/edge_computing/ztp-deploying-far-edge-clusters-
  at-scale.html
- TALM cluster upgrades.
  https://docs.okd.io/{version}/edge_computing/cnf-talm-for-cluster-updates.html
- Low latency tuning for RAN.
  https://docs.okd.io/{version}/scalability_and_performance/low_latency_tuning/c
  nf-tuning-low-latency-nodes-with-perf-profile.html

#### Scalability & Performance

- Index.
  https://docs.okd.io/{version}/scalability_and_performance/index.html
- Node Tuning Operator.
  https://docs.okd.io/{version}/scalability_and_performance/using-node-tuning-
  operator.html
- Performance Addon / PerformanceProfile.
  https://docs.okd.io/{version}/scalability_and_performance/low_latency_tuning/c
  nf-create-performance-profiles.html
- Huge pages.
  https://docs.okd.io/{version}/scalability_and_performance/what-huge-pages-do-
  and-how-they-are-consumed-by-apps.html

#### Security

- Security index.
  https://docs.okd.io/{version}/security/index.html
- SCC (Security Context Constraints).
  https://docs.okd.io/{version}/authentication/managing-security-context-
  constraints.html
- Certificates.
  https://docs.okd.io/{version}/security/certificates/replacing-default-ingress-
  certificate.html
- Compliance Operator.
  https://docs.okd.io/{version}/security/compliance_operator/co-overview.html
- File Integrity Operator.
  https://docs.okd.io/{version}/security/file_integrity_operator/file-integrity-
  operator-understanding.html

#### Authentication & Authorization

- Identity providers.
  https://docs.okd.io/{version}/authentication/understanding-identity-
  provider.html
- RBAC.
  https://docs.okd.io/{version}/authentication/using-rbac.html

#### Monitoring

- Monitoring overview.
  https://docs.okd.io/{version}/monitoring/monitoring-overview.html
- Alerting.
  https://docs.okd.io/{version}/monitoring/managing-alerts.html

#### Logging

- Logging overview.
  https://docs.okd.io/{version}/logging/cluster-logging.html
- LokiStack.
  https://docs.okd.io/{version}/logging/log_storage/installing-log-storage.html

#### CLI Tools

- oc CLI reference.
  https://docs.okd.io/{version}/cli_reference/openshift_cli/getting-started-
  cli.html
- oc adm.
  https://docs.okd.io/{version}/cli_reference/openshift_cli/administrator-cli-
  commands.html
- oc-mirror v2.
  https://docs.okd.io/{version}/cli_reference/oc-mirror-v2/oc-
  mirror-v2-intro.html

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
  targeting OKD 4.19 / CentOS Stream CoreOS) as a compliant O-RAN O-Cloud
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
<<<<<<< Updated upstream
- **ocloud_platform_multi_cluster_observability**: Deploys the Multi-Cluster
  Observability Operator to provide metrics and monitoring across managed
  clusters.
- **ocloud_platform_cluster_group_upgrades**: Installs the
  TALM/cluster-group-upgrades operator, facilitating automated and orchestrated
  upgrades of OKD clusters as required for O-Cloud lifecycle.
- **ocloud_platform_file_integrity**: Deploys and configures the File Integrity
  Operator to monitor and protect file system integrity on OKD nodes.
- **ocloud_postinstall_validation**: Runs post-install checks and workload
  deployments to validate conformance with O-RAN O-Cloud requirements.
=======
- **ocloud_platform_mco**: Deploys the Multi-Cluster Observability Operator to
  provide metrics and monitoring across managed clusters.
- **ocloud_platform_cgu**: Installs the TALM/cluster-group-upgrades operator,
  facilitating automated and orchestrated upgrades of OKD clusters as required
  for O-Cloud lifecycle.
>>>>>>> Stashed changes

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
  `manifests-examples/clusterimagesets/4.19.0-okd-scos.19.yaml`) live under
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