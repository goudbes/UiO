cmake_minimum_required(VERSION 3.5)
project(Hjemmeeksamen)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES_CLIENT client.c job.h common.h)
add_executable(Hjemmeeksamen_client ${SOURCE_FILES_CLIENT})

set(SOURCE_FILES_SERVER server.c job.h common.h)
add_executable(Hjemmeeksamen_server ${SOURCE_FILES_SERVER})
