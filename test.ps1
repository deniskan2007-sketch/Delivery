$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force build/classes | Out-Null
    $sourceFiles = @(Get-ChildItem src/delivery/*.java | ForEach-Object { $_.FullName })
    & javac -encoding UTF-8 --release 17 -d build/classes $sourceFiles test/delivery/DeliveryTests.java
    if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
    & java '-Dfile.encoding=UTF-8' -cp build/classes delivery.DeliveryTests
    if ($LASTEXITCODE -ne 0) { throw 'Tests failed' }
} finally { Pop-Location }
