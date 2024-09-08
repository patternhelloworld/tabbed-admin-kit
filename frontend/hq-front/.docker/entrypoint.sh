#!/bin/bash
cat /etc/nginx-template/app.conf.ctmpl > /etc/nginx/conf.d/default.conf
/usr/sbin/nginx -t && exec /usr/sbin/nginx -g "daemon off;"
