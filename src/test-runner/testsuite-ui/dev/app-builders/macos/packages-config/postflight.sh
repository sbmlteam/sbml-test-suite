#!/bin/sh -x
# Post-installation script for use with WhiteBox's Packages
# for SBML Test Runner installation on macOS.

# The list of arguments is described in
# http://s.sudre.free.fr/Stuff/PackageMaker_Howto.html

this_script="$0"
package_path="$1"
target_location="$2"
target_volume="$3"

/usr/bin/open "$target_location"
/usr/bin/osascript <<EOF
tell application "Finder"
  activate
  tell the front Finder window
    set the current view to icon view
  end tell
end tell
tell application "System Events" to tell process "Finder"
    set size of front window to {900, 300}
end tell
EOF
exit 0
