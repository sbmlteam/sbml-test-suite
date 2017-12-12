2013-02-21 <mhucka@caltech.edu>

This directory contains the source files for the images used to generate the
Mac OS X application icon for the SBML Test Runner.  It was not obvious (to
me anyway) how to create the high-resolution icons for Mountain Lion; Apple
essentially hid the previous tool, Icon Composer, inside some harder-to-find
location in Xcode, apparently because it is no longer the recommended way of
creating icons.  So how do you create icons?  Below is the procedure I
finally found that worked, based on this posting on StackOverflow:

  http://apple.stackexchange.com/questions/59561/where-did-icon-composer-go-from-xcode

1. Create a folder with the extension .iconset

2. Create the following files and put them in the folder (yes, name them this way):

    icon_16x16.png
    icon_16x16@2x.png
    icon_32x32.png
    icon_32x32@2x.png
    icon_128x128.png
    icon_128x128@2x.png
    icon_256x256.png
    icon_256x256@2x.png
    icon_512x512.png
    icon_512x512@2x.png

   (Note: the @2x versions are double-resolution.  In other words, 16x16@2x
   is actually just 32x32.  This means that for most of them, you can simply
   duplicate the larger versions and rename them to have the @2x suffix.)

3. Run this command:

   iconutil -c icns ICONSET_FILENAME

   where ICONSET_FILENAME is the folder containing the image files.  The
   result of this command should be a package (which is actually a directory)
   with the extension .icns.
