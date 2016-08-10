#!/bin/bash
DIRECTORY=$(cd `dirname $0` && pwd)
STAGE_DIR="$DIRECTORY/stage"
DMG="$DIRECTORY/SBML_Test_Runner.dmg"
APP_DIR="$STAGE_DIR/SBML Test Runner.app"
STUB="/usr/share/java/Tools/Jar Bundler.app/Contents/MacOS/JavaApplicationStub"

cd $DIRECTORY
ant stage-osx32

if [ -d "$APP_DIR" ]; then
	rm -rf "$APP_DIR"
fi

mkdir "$APP_DIR"
mkdir "$APP_DIR/Contents"
mkdir "$APP_DIR/Contents/MacOS"
mkdir "$APP_DIR/Contents/Resources"
mkdir "$APP_DIR/Contents/Resources/Java"
cp $STAGE_DIR/*jar "$APP_DIR/Contents/Resources/Java"
cp "$DIRECTORY/Info.plist" "$APP_DIR/Contents"
cp "$DIRECTORY/PkgInfo" "$APP_DIR/Contents"
cp "$DIRECTORY/src/org/sbml/testsuite/ui/resources/SBML Test Runner logo.icns" "$APP_DIR/Contents/Resources/GenericApp.icns"
cp "$STUB" "$APP_DIR/Contents/MacOS"

hdiutil create -ov -srcfolder "$APP_DIR" "$DMG"
hdiutil internet-enable -yes "$DMG"


