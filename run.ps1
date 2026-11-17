& "$PSScriptRoot/build.ps1"
if (-not $?) { exit 1 }
& java '-Dfile.encoding=UTF-8' -cp "$PSScriptRoot/build/classes" delivery.Main
