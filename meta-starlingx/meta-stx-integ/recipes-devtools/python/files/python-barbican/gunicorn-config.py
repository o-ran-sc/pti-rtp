import multiprocessing

bind = '0.0.0.0:9311'
user = 'barbican'
group = 'barbican'

timeout = 30
backlog = 2048
keepalive = 2

workers = multiprocessing.cpu_count() * 2

loglevel = 'info'
errorlog = '-'
accesslog = '-'

