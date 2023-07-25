#!/bin/sh

# La classe qui contient la méthode main ne doit pas hériter de Application

src=~/shared/SVN/trunk/src
linux=/usr/lib/jvm/java-11-openjdk
windows=~/Downloads/windows-javafx-sdk-11.0.2
osx=~/Downloads/osx-javafx-sdk-11.0.2

[ "$1" ] && dir="$1" || dir=kakurofx-jargen

mkdir -- "$dir" || exit 1
cd -- "$dir" || exit 1

# Pour javac, lib ou jmods possible
javac -p "$linux"/lib -d . "$src"/module-info.java "$src"/kakurofx/*.java

cp -i -r "$src"/kakurofx/resources kakurofx

for path in "$linux" "$windows" "$osx"; do
	for i in base graphics controls fxml; do unzip -q -n "$path"/lib/javafx."$i".jar; done
done

cp -i "$linux"/lib/{libprism*.so,libjavafx*.so,libglass*.so,libdecora_sse.so} .
cp -i "$windows"/bin/{prism*.dll,javafx*.dll,glass.dll,decora_sse.dll} .
cp -i "$osx"/lib/{libprism*.dylib,libjavafx*.dylib,libglass.dylib,libdecora_sse.dylib} .

rm META-INF/MANIFEST.MF module-info.class

jar --create --file=kakurofx.jar --main-class=kakurofx.MainJar .

