# Setup HTTP Store

Sets up a web host which can be used to distribute ISO's for `boot_iso` role

## Role Variables

| Variable                  | Required | Default                                                  | Comments                                                                                                                              |
|---------------------------|----------|----------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| http_store_container_name | no       | http_store                                               |                                                                                                                                       |
| http_store_pod_name       | no       | http_store_pod                                           |                                                                                                                                       |
| http_dir                  | no       | /opt/http_store                                          |                                                                                                                                       |
| http_data_dir             | no       | "{{ http_dir }}/data"                                    |                                                                                                                                       |
| container_image           | no       | registry.centos.org/centos/httpd-24-centos7:latest       | If you change this to anything other than the same image on a different host you may need to change then environment vars in the task |
| http_store_dir            | no       | /opt/http_store/data                                     |
| http_port                 | no       | int 80                                                   |
| http_host                 | no       | {{ 'http://' + hostvars['http_store']['ansible_host'] }} |
| test_file_name            | no       | "http_test"                                              |

## Dependencies

- containers.podman

## Example Playbook

```yaml
- name: Install and http_store service
  hosts: http_store
  roles:
    - setup_http_store
  vars:
    http_store_container_name: "iso store"
```
