#
# Copyright (C) 2019 Wind River Systems, Inc.
#

inherit ${@bb.utils.contains('GOVERSION', '1.2.%', 'go112', '', d)}
