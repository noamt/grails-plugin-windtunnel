#!/bin/bash

LAUNCHER_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
java -cp "${LAUNCHER_DIR}/lib/*" -Dlauncher.dir="$LAUNCHER_DIR" org._10ne.grails.windtunnel.executor.Main $1