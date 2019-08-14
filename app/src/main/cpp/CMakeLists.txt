cmake_minimum_required(VERSION 3.4.1)

#配置指定路径下的详细或者全部文件
file(GLOB bzip2_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

#设置本地的动态库编译生成
add_library(
             native-lib # 模块名

             SHARED # 动态库

           native-lib.cpp # 源文件
           bspatch.c# 源文件
           ${bzip2_source})

#系统库 log
find_library(
              log-lib

              log )


target_link_libraries(
                       native-lib

                       ${log-lib} )