# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)
project("libyuv")
#外部头文件路径
include_directories(libyuv/include)
#添加子项目，libyuv作为一个子项目自己编译，有自己的CMakeList.txt
#编译结果存放在build目录下，可以在里边找到生成的.so文件
add_subdirectory(libyuv ./build)
# Declares and names the project.

#aux_source_directory(src/main/cpp SRC_FILE)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#生成动态链接库yuvutil,YuvJni.cpp是源代码，可以指定多个源文件
add_library( # Sets the name of the library.
            yuv_utils

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             YuvJni.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
#添加NDK里边编译好的log库
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
#把yuv（这个是libyuv子项目生成的yuv.so）和log库连接到libyuv中
target_link_libraries( # Specifies the target library.
                       yuv_utils
                       yuv
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )