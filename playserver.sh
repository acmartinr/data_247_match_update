#!/bin/bash
#
# --------------------------------------------------------------------
# This is a free shell script under GNU GPL version 3.0 or above
# Copyright (C) 2005 ReFlectiv project.
# Feedback/comment/suggestions : http://www.reflectiv.net/
# -------------------------------------------------------------------------
#
# This scripts do the start/stop/restart of a PlayFramework project with GIT Support
#

### BEGIN vars
PORT=9000
BASE_DIR=/home/martin/makeMyDataServer/data_247/
CONF_PATH=/home/martin/makeMyDataServer/data_247/conf/application.conf
PLAY_VERSION=2.1.5 # We assume Play is in /opt/play/{version}, eg: /opt/play/2.1.5/

# Specific project configuration environment :
export _JAVA_OPTIONS="-Xms1024m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m"
### END vars

# Exit immediately if a command exits with a nonzero exit status.
set -e

update() {
    echo "Updating"

    cd $BASE_DIR || exit

    unset GIT_DIR
    # Update repo
    git pull origin appMakemyDataOficial

    cd $BASE_DIR
    # Creating new project (MUST BE ON THE GOOD DIR)
    /home/play-2.0/play clean compile stage
}

start() {
    echo "Starting server"
    eval $BASE_DIR$ENV"/target/start -Dhttp.port="$PORT" -Dconfig.file="$CONF_PATH" &"
}

stop() {
    echo "Stopping server"
    if [ -f $BASE_DIR"/RUNNING_PID" ];then
        kill `cat $BASE_DIR$ENV"/RUNNING_PID"`
    fi
}

case "$1" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    update)
        update
    ;;
    force-restart)
        stop
        update
        start
    ;;
    *)
        echo $"Usage: $0 {start|force-restart|stop|restart|update}"
esac

exit 0
