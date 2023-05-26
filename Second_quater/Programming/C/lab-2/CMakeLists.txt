cmake_minimum_required(VERSION 3.24)
project(lab_2 C)

set(CMAKE_C_STANDARD 11)

#add_subdirectory(libdeflate)

add_executable(lab_2 main.c)

target_link_libraries(lab_2 z)
#target_link_libraries(lab_2 libdeflate)