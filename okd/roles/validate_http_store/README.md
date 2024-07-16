# validate_http_store

Checks via a round trip that HTTP store is functional


variables

| name | type | default |
|======|======|=========|
| http_store_dir | string | /opt/http_store/data |
| http_port | int | 80 |
| http_host | string | {{ 'http://' + hostvars['http_store']['ansible_host'] }} |
| test_file_name | string | http_test |
