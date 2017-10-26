#!/bin/sh
# The next line is a fix for an SWT interaction bug on Ubuntu 16.
# See https://askubuntu.com/questions/761604/eclipse-not-working-in-16-04
SWT_GTK3=0
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
java=java
exec "$java" $java_args -jar "$MYSELF" "$@"
exit 1
