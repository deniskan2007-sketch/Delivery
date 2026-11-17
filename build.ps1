$ErrorActionPreference = 'Stop'
$projectDir = $PSScriptRoot
Push-Location $projectDir
try {
    New-Item -ItemType Directory -Force build/classes | Out-Null
    $sourceFiles = @(Get-ChildItem src/delivery/*.java | ForEach-Object { $_.FullName })
    & javac -encoding UTF-8 --release 17 -d build/classes $sourceFiles
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
} finally { Pop-Location }
