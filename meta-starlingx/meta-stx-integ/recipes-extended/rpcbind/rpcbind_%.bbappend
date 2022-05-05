USERADD_PARAM_${PN} = "-r -g rpc -u 32 -d /var/lib/rpcbind -s /sbin/nologin -c 'Rpcbind Daemons' rpc"
GROUPADD_PARAM_${PN} = "-r -g 32 rpc"
