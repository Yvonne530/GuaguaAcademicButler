# E2E test: create -> list(should be visible) -> delete -> list(should be gone) -> re-delete(400)
$ErrorActionPreference='Stop'
$base='http://localhost:8080'
$r=Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" -ContentType 'application/json' -Body (@{username='admin';password='admin123';userType='admin'}|ConvertTo-Json)
$tok=$r.data.accessToken; $h=@{Authorization="Bearer $tok";'Accept'='application/json'}
function Step($n,$ok){ Write-Output ("[{0}] {1}" -f ($(if($ok){'PASS'}else{'FAIL'})), $n) }

$name="E2ETestDept_" + (Get-Random)
$c=Invoke-RestMethod -Method Post -Uri "$base/api/departments" -Headers $h -ContentType 'application/json' -Body (@{name=$name;description='e2e'}|ConvertTo-Json)
$id=$c.data.id
Step "1. POST create returns id=$id" ($c.code -eq 201 -and $id)
$l=Invoke-RestMethod -Method Get -Uri "$base/api/departments" -Headers $h
Step "2. new dept visible in list (cache bug check)" ($null -ne ($l.data | Where-Object { $_.id -eq $id }))
Invoke-RestMethod -Method Delete -Uri "$base/api/departments/$id" -Headers $h | Out-Null
Step "3. DELETE ok" $true
$l2=Invoke-RestMethod -Method Get -Uri "$base/api/departments" -Headers $h
Step "4. dept gone from list after delete" ($null -eq ($l2.data | Where-Object { $_.id -eq $id }))
try { Invoke-WebRequest -Method Delete -Uri "$base/api/departments/$id" -Headers $h -UseBasicParsing | Out-Null; Step "5. re-delete returns 400" $false }
catch { Step "5. re-delete returns 400 (not found)" ([int]$_.Exception.Response.StatusCode -eq 400) }
