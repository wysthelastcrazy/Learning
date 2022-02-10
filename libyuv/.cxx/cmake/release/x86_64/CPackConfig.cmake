# This file will be configured to contain variables for CPack. These variables
# should be set in the CMake list file of the project before CPack module is
# included. The list of available CPACK_xxx variables and their associated
# documentation may be obtained using
#  cpack --help-variable-list
#
# Some variables are common to all generators (e.g. CPACK_PACKAGE_NAME)
# and some are specific to a generator
# (e.g. CPACK_NSIS_EXTRA_INSTALL_COMMANDS). The generator specific variables
# usually begin with CPACK_<GENNAME>_xxxx.


SET(CPACK_BINARY_7Z "")
SET(CPACK_BINARY_BUNDLE "")
SET(CPACK_BINARY_CYGWIN "")
SET(CPACK_BINARY_DEB "")
SET(CPACK_BINARY_DRAGNDROP "")
SET(CPACK_BINARY_FREEBSD "")
SET(CPACK_BINARY_IFW "")
SET(CPACK_BINARY_NSIS "")
SET(CPACK_BINARY_OSXX11 "")
SET(CPACK_BINARY_PACKAGEMAKER "")
SET(CPACK_BINARY_PRODUCTBUILD "")
SET(CPACK_BINARY_RPM "")
SET(CPACK_BINARY_STGZ "")
SET(CPACK_BINARY_TBZ2 "")
SET(CPACK_BINARY_TGZ "")
SET(CPACK_BINARY_TXZ "")
SET(CPACK_BINARY_TZ "")
SET(CPACK_BINARY_WIX "")
SET(CPACK_BINARY_ZIP "")
SET(CPACK_BUILD_SOURCE_DIRS "D:/workspace/Learning/libyuv/src/main/cpp;D:/workspace/Learning/libyuv/.cxx/cmake/release/x86_64")
SET(CPACK_CMAKE_GENERATOR "Ninja")
SET(CPACK_COMPONENT_UNSPECIFIED_HIDDEN "TRUE")
SET(CPACK_COMPONENT_UNSPECIFIED_REQUIRED "TRUE")
SET(CPACK_DEBIAN_PACKAGE_MAINTAINER "Frank Barchard <fbarchard@chromium.org>")
SET(CPACK_DEBIAN_PACKAGE_PRIORITY "optional")
SET(CPACK_DEBIAN_PACKAGE_SECTION "other")
SET(CPACK_GENERATOR "DEB;RPM")
SET(CPACK_INSTALL_CMAKE_PROJECTS "D:/workspace/Learning/libyuv/.cxx/cmake/release/x86_64;libyuv;ALL;/")
SET(CPACK_INSTALL_PREFIX "C:/Program Files (x86)/libyuv")
SET(CPACK_MODULE_PATH "")
SET(CPACK_NSIS_DISPLAY_NAME "libyuv 0.0.")
SET(CPACK_NSIS_INSTALLER_ICON_CODE "")
SET(CPACK_NSIS_INSTALLER_MUI_ICON_CODE "")
SET(CPACK_NSIS_INSTALL_ROOT "$PROGRAMFILES")
SET(CPACK_NSIS_PACKAGE_NAME "libyuv 0.0.")
SET(CPACK_OUTPUT_CONFIG_FILE "D:/workspace/Learning/libyuv/.cxx/cmake/release/x86_64/CPackConfig.cmake")
SET(CPACK_PACKAGE_CONTACT "fbarchard@chromium.org")
SET(CPACK_PACKAGE_DEFAULT_LOCATION "/")
SET(CPACK_PACKAGE_DESCRIPTION "YUV library and YUV conversion tool")
SET(CPACK_PACKAGE_DESCRIPTION_FILE "D:/Android/Sdk/cmake/3.10.2.4988404/share/cmake-3.10/Templates/CPack.GenericDescription.txt")
SET(CPACK_PACKAGE_DESCRIPTION_SUMMARY "YUV library")
SET(CPACK_PACKAGE_FILE_NAME "libyuv-0.0.-linux-amd-64")
SET(CPACK_PACKAGE_INSTALL_DIRECTORY "libyuv 0.0.")
SET(CPACK_PACKAGE_INSTALL_REGISTRY_KEY "libyuv 0.0.")
SET(CPACK_PACKAGE_NAME "libyuv")
SET(CPACK_PACKAGE_RELOCATABLE "true")
SET(CPACK_PACKAGE_VENDOR "Frank Barchard")
SET(CPACK_PACKAGE_VERSION "0.0.")
SET(CPACK_PACKAGE_VERSION_MAJOR "0")
SET(CPACK_PACKAGE_VERSION_MINOR "0")
SET(CPACK_PACKAGE_VERSION_PATCH "1")
SET(CPACK_RESOURCE_FILE_LICENSE "D:/workspace/Learning/libyuv/src/main/cpp/libyuv/LICENSE")
SET(CPACK_RESOURCE_FILE_README "D:/Android/Sdk/cmake/3.10.2.4988404/share/cmake-3.10/Templates/CPack.GenericDescription.txt")
SET(CPACK_RESOURCE_FILE_WELCOME "D:/Android/Sdk/cmake/3.10.2.4988404/share/cmake-3.10/Templates/CPack.GenericWelcome.txt")
SET(CPACK_SET_DESTDIR "OFF")
SET(CPACK_SOURCE_7Z "")
SET(CPACK_SOURCE_CYGWIN "")
SET(CPACK_SOURCE_GENERATOR "TBZ2;TGZ;TXZ;TZ")
SET(CPACK_SOURCE_OUTPUT_CONFIG_FILE "D:/workspace/Learning/libyuv/.cxx/cmake/release/x86_64/CPackSourceConfig.cmake")
SET(CPACK_SOURCE_RPM "OFF")
SET(CPACK_SOURCE_TBZ2 "ON")
SET(CPACK_SOURCE_TGZ "ON")
SET(CPACK_SOURCE_TXZ "ON")
SET(CPACK_SOURCE_TZ "ON")
SET(CPACK_SOURCE_ZIP "OFF")
SET(CPACK_SYSTEM_NAME "linux-amd-64")
SET(CPACK_TOPLEVEL_TAG "linux-amd-64")
SET(CPACK_WIX_SIZEOF_VOID_P "8")

if(NOT CPACK_PROPERTIES_FILE)
  set(CPACK_PROPERTIES_FILE "D:/workspace/Learning/libyuv/.cxx/cmake/release/x86_64/CPackProperties.cmake")
endif()

if(EXISTS ${CPACK_PROPERTIES_FILE})
  include(${CPACK_PROPERTIES_FILE})
endif()
