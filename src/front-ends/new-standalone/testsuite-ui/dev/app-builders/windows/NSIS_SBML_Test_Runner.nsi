; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "${NAME}"
!define PRODUCT_VERSION "${VERSION}"
!define PRODUCT_PUBLISHER "SBML Team"
!define PRODUCT_WEB_SITE "${WEBSITE}"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\${PRODUCT_NAME}"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${DIST_DIR}\..\src\data\application-icons\windows\Uninstall_SBML_Test_Runner_icon.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
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

; MUI end ------

!include "x64.nsh"

!define PRODUCT_RUN32 "SBML Test Runner - Windows x86.exe"
!define PRODUCT_RUN64 "SBML Test Runner - Windows x64.exe"
!define SM_FOLDER "SBML Test Runner"

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "${DIST_DIR}\Setup_SBMLTestRunner-${PRODUCT_VERSION}-Windows.exe"
InstallDir "$PROGRAMFILES\SBML\SBML Test Runner"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "MainSection" SEC01
    SetOutPath "$INSTDIR"
    SetOverwrite try

    File "${DIST_DIR}\SBML Test Runner - Windows x86.exe"
    File "${DIST_DIR}\SBML Test Runner - Windows x64.exe"
    File "${DIST_DIR}\..\..\COPYING.txt"
    File "${DIST_DIR}\..\..\LICENSE.txt"
    File "${DIST_DIR}\..\README.txt"
    File "${DIST_DIR}\..\NEWS.txt"
    File "${DIST_DIR}\DetectJVM.jar"

    CreateDirectory "$SMPROGRAMS\${SM_FOLDER}"
SectionEnd

Section -AdditionalIcons
    WriteIniStr "$INSTDIR\SBML Test Suite website.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
    CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\SBML Test Suite website\Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
    CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\Uninstall.lnk" "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
SectionEnd

Section -Post
    WriteUninstaller "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    ${If} ${RunningX64}
        WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    ${If} ${RunningX64}
        WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRODUCT_RUN64}"
    ${Else}
        WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRODUCT_RUN32}"
    ${EndIf}
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
    WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd

Function LaunchLink
    ${If} ${RunningX64}
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
;
; This is based on http://nsis.sourceforge.net/A_slightly_better_Java_Launcher
; but with modifications. [2013-05-21 <mhucka@caltech.edu>].
;
; Find JRE (javaw.exe)
; 1 - in .\jre directory (if a JRE is installed with this application)
; 2 - in JAVA_HOME environment variable
; 3 - in the registry
; 4 - assume javaw.exe in current dir or PATH
 
    Push $R0
    Push $R1

    ClearErrors
    StrCpy $R0 "$EXEDIR\jre\bin\javaw.exe"
    IfFileExists $R0 JreFound
    StrCpy $R0 ""

    ClearErrors
    ReadEnvStr $R0 "JAVA_HOME"
    StrCpy $R0 "$R0\bin\javaw.exe"
    IfErrors 0 JreFound

    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\javaw.exe"

    IfErrors 0 JreFound
    StrCpy $R0 "javaw.exe"

    JreFound:
        Pop $R1
        Exch $R0
FunctionEnd

Function CreateDesktopShortcut
    ClearErrors
    Call FindJava
    Pop $R0
    ExecWait '$R0 -jar "$INSTDIR\DetectJVM.jar"' $0
    IfErrors DetectExecError
    Delete "$INSTDIR\DetectJVM.jar"
    IntCmp $0 0 DetectError DetectError DoneDetect
    DetectExecError:
        StrCpy $0 "exec error ($R0)"
    DetectError:
        MessageBox MB_OK "Could not determine JVM architecture ($0). Assuming 32-bit."
        Goto NotX64
    DoneDetect:
        IntCmp $0 64 X64 NotX64 NotX64
    X64:
        CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN64}"
        CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN64}"
        Goto DoneX64
    NotX64:
        CreateShortCut "$SMPROGRAMS\${SM_FOLDER}\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN32}"
        CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_RUN32}"
    DoneX64:
FunctionEnd

Section Uninstall
    Delete "$INSTDIR\SBML Test Suite website.url"
    Delete "$INSTDIR\Uninstall ${PRODUCT_NAME}.exe"
    Delete "$INSTDIR\${PRODUCT_RUN64}"
    Delete "$INSTDIR\${PRODUCT_RUN32}"
    Delete "$INSTDIR\COPYING.txt"
    Delete "$INSTDIR\LICENSE.txt"
    Delete "$INSTDIR\README.txt"
    Delete "$INSTDIR\NEWS.txt"
    Delete "$INSTDIR\DetectJVM.jar"

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
