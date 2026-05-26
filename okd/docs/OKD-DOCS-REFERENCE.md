# OKD Documentation Reference

**Purpose:** Quick reference to versioned OKD documentation for O-Cloud deployment  
**Maintained by:** O-RAN SC INF team

---

## Overview

This document provides links to official OKD documentation across multiple versions.
Use this reference when:
- Looking up specific OKD features or configuration
- Understanding version differences between OKD releases
- Finding documentation for operators, networking, storage, etc.

**Current target version for pti-rtp okd automation:** OKD 4.22 / SCOS

---

## Versioned References

Primary docs: https://docs.okd.io/
Use https://docs.okd.io/latest/ for the current release at any time.

- Version 4.21: Supported previous version.
  https://docs.okd.io/4.21/welcome/index.html
- Version 4.22: Target version in pti-rtp okd automation (OKD 4.22 / SCOS).
  https://docs.okd.io/4.22/welcome/index.html
- Latest: Latest available release.
  https://docs.okd.io/latest/welcome/index.html

---

## Documentation Structure

Each version organizes its docs into the following top-level sections.
When answering questions, link to the specific version requested or default to
`latest`.
Replace `{version}` in URLs below with e.g. `4.19`, `4.20`, or `latest`.

### Overview / Welcome

- Product introduction and Kubernetes overview
- OpenShift editions comparison
- Welcome index.
  https://docs.okd.io/{version}/welcome/index.html

### What's New

- New features and enhancements per release
- Deprecated features (check before upgrading automation)
- New features.
  https://docs.okd.io/{version}/whats_new/new-features.html
- Deprecated features.
  https://docs.okd.io/{version}/whats_new/deprecated-features.html

### Architecture

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

### Disconnected Environments

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
  https://docs.okd.io/{version}/disconnected/installing-mirroring-creating-registry.html

### Installing

- Installation overview.
  https://docs.okd.io/{version}/installing/overview/index.html
- Cluster capabilities (optional components).
  https://docs.okd.io/{version}/installing/overview/cluster-capabilities.html
- Bare metal IPI.
  https://docs.okd.io/{version}/installing/installing_bare_metal_ipi/ipi-install-overview.html
- Bare metal UPI.
  https://docs.okd.io/{version}/installing/installing_bare_metal/installing-bare-metal.html
- SNO (Single-Node OpenShift).
  https://docs.okd.io/{version}/installing/installing_sno/install-sno-installing-sno.html
- Platform-agnostic (agent-based).
  https://docs.okd.io/{version}/installing/installing_with_agent_based_installer/preparing-to-install-with-agent-based-installer.html
- AWS IPI.
  https://docs.okd.io/{version}/installing/installing_aws/ipi/installing-aws-default.html
- Azure IPI.
  https://docs.okd.io/{version}/installing/installing_azure/ipi/installing-azure-default.html
- vSphere IPI.
  https://docs.okd.io/{version}/installing/installing_vsphere/ipi/ipi-vsphere-installation-reqs.html
- On-premises (bare metal) is the relevant path for O-RAN SC INF/OKD deployments

### Post-installation Configuration

- Cluster tasks.
  https://docs.okd.io/{version}/post_installation_configuration/cluster-tasks.html
- Machine config operator (MCO).
  https://docs.okd.io/{version}/post_installation_configuration/machine-configuration-tasks.html
- Node tuning (performance-addon/tuned).
  https://docs.okd.io/{version}/post_installation_configuration/node-tasks.html

### Updating Clusters

- Update overview.
  https://docs.okd.io/{version}/updating/understanding_updates/intro-to-updates.html
- CLI updates.
  https://docs.okd.io/{version}/updating/updating_a_cluster/updating-cluster-cli.html
- TALM (cluster-group-upgrades).
  https://docs.okd.io/{version}/edge_computing/cnf-talm-for-cluster-updates.html

### Operators / OLM

- OLM overview.
  https://docs.okd.io/{version}/operators/understanding/olm/olm-understanding-olm.html
- OperatorHub.
  https://docs.okd.io/{version}/operators/admin/olm-adding-operators-to-cluster.html
- Operator SDK.
  https://docs.okd.io/{version}/operators/operator_sdk/osdk-about.html

### Networking

- Networking overview.
  https://docs.okd.io/{version}/networking/understanding-networking.html
- OVN-Kubernetes.
  https://docs.okd.io/{version}/networking/ovn_kubernetes_network_provider/about-ovn-kubernetes.html
- SR-IOV (critical for O-RAN O-DU).
  https://docs.okd.io/{version}/networking/hardware_networks/about-sriov.html
- MetalLB.
  https://docs.okd.io/{version}/networking/metallb/about-metallb.html

### Storage

- Storage overview.
  https://docs.okd.io/{version}/storage/index.html
- Persistent volumes.
  https://docs.okd.io/{version}/storage/understanding-persistent-storage.html
- CSI drivers.
  https://docs.okd.io/{version}/storage/container_storage_interface/persistent-storage-csi.html

### Edge / Telco / RAN Specific

- Edge computing overview.
  https://docs.okd.io/{version}/edge_computing/index.html
- RAN DU reference design.
  https://docs.okd.io/{version}/edge_computing/ran_du_deployment_guide/ztp-ran-du-deployment-overview.html
- ZTP (Zero Touch Provisioning / GitOps).
  https://docs.okd.io/{version}/edge_computing/ztp-deploying-far-edge-clusters-at-scale.html
- Low latency tuning for RAN.
  https://docs.okd.io/{version}/scalability_and_performance/low_latency_tuning/cnf-tuning-low-latency-nodes-with-perf-profile.html

### Scalability & Performance

- Index.
  https://docs.okd.io/{version}/scalability_and_performance/index.html
- Node Tuning Operator.
  https://docs.okd.io/{version}/scalability_and_performance/using-node-tuning-operator.html
- Performance Addon / PerformanceProfile.
  https://docs.okd.io/{version}/scalability_and_performance/low_latency_tuning/cnf-create-performance-profiles.html
- Huge pages.
  https://docs.okd.io/{version}/scalability_and_performance/what-huge-pages-do-and-how-they-are-consumed-by-apps.html

### Security

- Security index.
  https://docs.okd.io/{version}/security/index.html
- SCC (Security Context Constraints).
  https://docs.okd.io/{version}/authentication/managing-security-context-constraints.html
- Certificates.
  https://docs.okd.io/{version}/security/certificates/replacing-default-ingress-certificate.html
- Compliance Operator.
  https://docs.okd.io/{version}/security/compliance_operator/co-overview.html
- File Integrity Operator.
  https://docs.okd.io/{version}/security/file_integrity_operator/file-integrity-operator-understanding.html

### Authentication & Authorization

- Identity providers.
  https://docs.okd.io/{version}/authentication/understanding-identity-provider.html
- RBAC.
  https://docs.okd.io/{version}/authentication/using-rbac.html

### Monitoring

- Monitoring overview.
  https://docs.okd.io/{version}/monitoring/monitoring-overview.html
- Alerting.
  https://docs.okd.io/{version}/monitoring/managing-alerts.html

### Logging

- Logging overview.
  https://docs.okd.io/{version}/logging/cluster-logging.html
- LokiStack.
  https://docs.okd.io/{version}/logging/log_storage/installing-log-storage.html

### CLI Tools

- oc CLI reference.
  https://docs.okd.io/{version}/cli_reference/openshift_cli/getting-started-cli.html
- oc adm.
  https://docs.okd.io/{version}/cli_reference/openshift_cli/administrator-cli-commands.html
- oc-mirror v2.
  https://docs.okd.io/{version}/cli_reference/oc-mirror-v2/oc-mirror-v2-intro.html

---
