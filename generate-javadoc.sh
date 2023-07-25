#!/bin/sh

[ "$1" ] && dir="$1" || dir=kakurofx-javadocgen

mkdir -- "$dir" || exit 1
path=$(realpath -- "$dir")

cd ~/shared/SVN/trunk/src || exit 1

javadoc -private --show-members private --show-types private -javafx -d "$path" -p /usr/lib/jvm/java-11-openjdk/lib --add-modules javafx.controls,javafx.fxml kakurofx

