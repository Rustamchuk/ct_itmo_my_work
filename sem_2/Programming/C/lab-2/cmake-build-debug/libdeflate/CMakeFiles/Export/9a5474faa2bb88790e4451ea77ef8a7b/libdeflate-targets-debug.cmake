#----------------------------------------------------------------
# Generated CMake target import file for configuration "Debug".
#----------------------------------------------------------------

# Commands may need to know the format version.
set(CMAKE_IMPORT_FILE_VERSION 1)

# Import target "libdeflate::libdeflate_static" for configuration "Debug"
set_property(TARGET libdeflate::libdeflate_static APPEND PROPERTY IMPORTED_CONFIGURATIONS DEBUG)
set_target_properties(libdeflate::libdeflate_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_DEBUG "C"
  IMPORTED_LOCATION_DEBUG "${_IMPORT_PREFIX}/lib/libdeflate.a"
  )

list(APPEND _cmake_import_check_targets libdeflate::libdeflate_static )
list(APPEND _cmake_import_check_files_for_libdeflate::libdeflate_static "${_IMPORT_PREFIX}/lib/libdeflate.a" )

# Import target "libdeflate::libdeflate_shared" for configuration "Debug"
set_property(TARGET libdeflate::libdeflate_shared APPEND PROPERTY IMPORTED_CONFIGURATIONS DEBUG)
set_target_properties(libdeflate::libdeflate_shared PROPERTIES
  IMPORTED_IMPLIB_DEBUG "${_IMPORT_PREFIX}/lib/libdeflate.dll.a"
  IMPORTED_LOCATION_DEBUG "${_IMPORT_PREFIX}/bin/libdeflate.dll"
  )

list(APPEND _cmake_import_check_targets libdeflate::libdeflate_shared )
list(APPEND _cmake_import_check_files_for_libdeflate::libdeflate_shared "${_IMPORT_PREFIX}/lib/libdeflate.dll.a" "${_IMPORT_PREFIX}/bin/libdeflate.dll" )

# Commands beyond this point should not need to know the version.
set(CMAKE_IMPORT_FILE_VERSION)
