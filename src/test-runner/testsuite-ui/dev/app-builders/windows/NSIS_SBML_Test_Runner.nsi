;
; @file   NSIS_SBML_Test_Runner.nsi
; @brief  NSIS configuration file
; @author Michael Hucka
; @author Frank T. Bergmann
;
; ----------------------------------------------------------------------------
; This file is part of the SBML Test Suite. Please visit http://sbml.org for
; more information about SBML, and the latest version of the SBML Test Suite.
;
; Copyright (C) 2009-2017 jointly by the following organizations:
; 1. California Institute of Technology, Pasadena, CA, USA
; 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
; 3. University of Heidelberg, Heidelberg, Germany
;
; Copyright (C) 2006-2008 by the California Institute of Technology,
; Pasadena, CA, USA
;
; Copyright (C) 2002-2005 jointly by the following organizations:
; 1. California Institute of Technology, Pasadena, CA, USA
; 2. Japan Science and Technology Agency, Japan
;
; This library is free software; you can redistribute it and/or modify it
; under the terms of the GNU Lesser General Public License as published by
; the Free Software Foundation. A copy of the license agreement is provided
; in the file named "LICENSE.txt" included with this software distribution
; and also available online as http://sbml.org/software/libsbml/license.html
; ----------------------------------------------------------------------------

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "${NAME}"
!define PRODUCT_VERSION "${VERSION}"
!define PRODUCT_PUBLISHER "SBML Team"
!define PRODUCT_WEB_SITE "${WEBSITE}"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\${PRODUCT_NAME}"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"

!include "MUI.nsh"
!include "dev\app-builders\windows\MUI_EXTRAPAGES.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${DIST_DIR}\..\src\data\application-icons\windows\Uninstall_SBML_Test_Runner_icon.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; Read me page
!insertmacro MUI_PAGE_README "${DIST_DIR}\CHANGES.rtf"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Start the ${PRODUCT_NAME}"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink"

!define MUI_FINISHPAGE_SHOWREADME ""
!define MUI_FINISHPAGE_SHOWREADME_CHECKED
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Create Desktop Shortcut"
!define MUI_FINISHPAGE_SHOWREADME_FUNCTION CreateDesktopShortcut
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

; Read me page config
 
;Set up install lang strings for 1st lang
${ReadmeLanguage} "${LANG_ENGLISH}" \
        "  About the SBML Test Runner" \
        "" \
        "$(^name)" \
        "$\n  Click on scrollbar arrows or page down to review the entire text."

;Set up uninstall lang strings for 1st lang
${Un.ReadmeLanguage} "${LANG_ENGLISH}" \
        "  About the SBML Test Runner" \
        "" \
        "$(^name)" \
        "$\n  Click on scrollbar arrows or page down to review the entire text."

; MUI end ------

!define PRODUCT_RUN32 "SBML Test Runner - Windows x86.exe"
!define PRODUCT_RUN64 "SBML Test Runner - Windows x64.exe"
!define SM_FOLDER "SBML Test Runner"

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "${DIST_DIR}\Setup_SBMLTestRunner-${PRODUCT_VERSION}-Windows.exe"
InstallDir "$PROGRAMFILES\SBML\SBML Test Runner"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Var Bits
Var JavaPath

Section "MainSection" SEC01
    SetOutPath "$INSTDIR"
    SetOverwrite try

    File "${DIST_DIR}\SBML Test Runner - Windows x86.exe"
    File "${DIST_DIR}\SBML Test Runner - Windows x64.exe"
    File "${DIST_DIR}\ABOUT.html"
    File "${DIST_DIR}\NEWS.html"
    File "${DIST_DIR}\COPYING.html"
    File "${DIST_DIR}\LICENSE.html"

    CreateDirectory "$SMPROGRAMS\${SM_FOLDER}"
    Call FindJava
SectionEnd


Section -AdditionalIcons
    WriteIniStr "$INSTDIR\SBML Test Suite website.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
    CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\SBML Test Suite website\Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
    CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\Uninstall.lnk" "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
SectionEnd


Section -Post
    WriteUninstaller "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    ${If} $Bits == "64"
        WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    ${If} $Bits == "64"
        WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function LaunchLink
    ${If} $Bits == "64"
        ExecShell "" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        ExecShell "" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
FunctionEnd


Function un.onUninstSuccess
    HideWindow
    MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd


Function un.onInit
    MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
    Abort
FunctionEnd


Function FindJava
    ; Ideas based on http://nsis.sourceforge.net/A_slightly_better_Java_Launcher
    ; 2013-05-21 <mhucka@caltech.edu>
    ;
    ; Find JRE (javaw.exe)
    ; 1 - in .\jre directory (if a JRE is installed with this application)
    ; 2 - in JAVA_HOME environment variable
    ; 3 - in the registry
    ; 4 - assume javaw.exe in current dir or PATH
    ;
    ; For 2-3, we try first with the registry view set to 64 bit mode, then if
    ; that fails, we try again with 32 bits.

    Push $R0
    Push $R1

    SetRegView 64

    ClearErrors
    ReadEnvStr $R0 "JAVA_HOME"
    StrCpy $JavaPath "$R0\bin\javaw.exe"
    IfErrors 0 JreFound64

    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $JavaPath "$R0\bin\javaw.exe"
    IfErrors 0 JreFound64

    SetRegView 32

    ClearErrors
    ReadEnvStr $R0 "JAVA_HOME"
    StrCpy $JavaPath "$R0\bin\javaw.exe"
    IfErrors 0 JreFound32

    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $JavaPath "$R0\bin\javaw.exe"
    IfErrors 0 JreFound32

    JreFound32:
        StrCpy $Bits "32"
        Goto Done

    JreFound64:
        StrCpy $Bits "64"
        Goto Done

    Done:
        Pop $R1
        Pop $R0
FunctionEnd


Function CreateDesktopShortcut
    ${If} $Bits == "64"
        CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN64}"
        CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN32}"
        CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
FunctionEnd


Section Uninstall
    Delete "$INSTDIR\SBML Test Suite website.url"
    Delete "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    Delete "$INSTDIR\${PRODUCT_RUN64}"
    Delete "$INSTDIR\${PRODUCT_RUN32}"
    Delete "$INSTDIR\COPYING.html"
    Delete "$INSTDIR\LICENSE.html"
    Delete "$INSTDIR\ABOUT.html"
    Delete "$INSTDIR\NEWS.html"

    Delete "$SMPROGRAMS\${SM_FOLDER}\${PRODUCT_NAME}.lnk"
    Delete "$SMPROGRAMS\${SM_FOLDER}\Uninstall.lnk"
    Delete "$SMPROGRAMS\${SM_FOLDER}\SBML Test Suite website.lnk"

    Delete "$DESKTOP\${PRODUCT_NAME}.lnk"

    RMDir /r "$SMPROGRAMS\${SM_FOLDER}"
    RMDir "$INSTDIR"

    DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
    DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
    SetAutoClose true
SectionEnd
