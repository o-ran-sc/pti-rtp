from docs_conf.conf import *
linkcheck_ignore = [
    'http://localhost.*',
    'http://127.0.0.1.*',
    'https://gerrit.o-ran-sc.org.*'
]

# Linkcheck robustness for slow third-party hosts (notably StarlingX,
# OpenStack, and various standards-body sites referenced from this
# project's docs). Default Sphinx timeout is short enough that a slow
# upstream causes transient 'timeout' results which 'sphinx-build -W -b
# linkcheck' promotes to fatal errors.
linkcheck_retries = 3
linkcheck_timeout = 60
