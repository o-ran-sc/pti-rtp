# apply_nmstate

Applies nmstate network configuration to a host.

## Variables

| Variable                 | Default  | Required             | Description                                                                                                  |
|--------------------------|----------|----------------------|--------------------------------------------------------------------------------------------------------------|
| vm_nmstate_config_path   | -        | Yes                  | The path to output an nmstate yaml file                                                                      |
| rendered_nmstate_yml     | -        | Yes                  | The nmstate yaml file to apply                                                                               |
| vm_network_test_ip       | -        | Depends on the host  | An IP to check outbound connectivity post application. This is required if host is the Ansible control node. |



