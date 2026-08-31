$ErrorActionPreference='SilentlyContinue'
$base='http://localhost:8080'
function Login($u,$p,$t){ $r=Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" -ContentType 'application/json' -Body (@{username=$u;password=$p;userType=$t}|ConvertTo-Json); return $r.data.accessToken }
$admin=Login 'admin' 'admin123' 'admin'
$teacher=Login 'teacher1' '123456' 'teacher'
$student=Login 'student1' '123456' 'student'
$adminRefresh=(Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" -ContentType 'application/json' -Body (@{username='admin';password='admin123';userType='admin'}|ConvertTo-Json)).data.refreshToken
Write-Output "TOKENS: admin=$(if($admin){'OK'}else{'FAIL'}) teacher=$(if($teacher){'OK'}else{'FAIL'}) student=$(if($student){'OK'}else{'FAIL'})"

$tests=@(
 @{n='POST /api/auth/refresh';m='Post';u='/api/auth/refresh';tok='adminRefresh';body=@{userType='admin'}},
 @{n='GET /api/score/student/1';m='Get';u='/api/score/student/1';tok='admin'},
 @{n='GET /api/grade/view';m='Get';u='/api/grade/view';tok='student'},
 @{n='POST /api/grade/submit';m='Post';u='/api/grade/submit';tok='teacher';body=@{studentId='student2';courseCode='CS101';semester='S-2025';score=90}},
 @{n='POST /api/grade/weights/1';m='Post';u='/api/grade/weights/1';tok='teacher';body='[{"assessmentType":"FINAL","weight":100,"itemName":"TestX"}]'},
 @{n='GET /api/departments';m='Get';u='/api/departments';tok='admin'},
 @{n='GET /api/departments/1';m='Get';u='/api/departments/1';tok='admin'},
 @{n='POST /api/departments';m='Post';u='/api/departments';tok='admin';body=@{name='TestX';description='t'}},
 @{n='PUT /api/departments/1';m='Put';u='/api/departments/1';tok='admin';body=@{name='DeptUpd';description='t'}},
 @{n='DELETE /api/departments/999';m='Delete';u='/api/departments/999';tok='admin'},
 @{n='GET /api/majors';m='Get';u='/api/majors';tok='admin'},
 @{n='GET /api/majors/1';m='Get';u='/api/majors/1';tok='admin'},
 @{n='GET /api/majors/department/1';m='Get';u='/api/majors/department/1';tok='admin'},
 @{n='POST /api/majors';m='Post';u='/api/majors';tok='admin';body=@{name='TestX';departmentId=1}},
 @{n='PUT /api/majors/2';m='Put';u='/api/majors/2';tok='admin';body=@{name='TestX';departmentId=1}},
 @{n='DELETE /api/majors/999';m='Delete';u='/api/majors/999';tok='admin'},
 @{n='GET /api/permissions';m='Get';u='/api/permissions';tok='admin'},
 @{n='GET /api/permissions/1';m='Get';u='/api/permissions/1';tok='admin'},
 @{n='POST /api/permissions';m='Post';u='/api/permissions';tok='admin';body=@{name='TestX';description='t'}},
 @{n='PUT /api/permissions/1';m='Put';u='/api/permissions/1';tok='admin';body=@{name='TestX';description='t'}},
 @{n='DELETE /api/permissions/999';m='Delete';u='/api/permissions/999';tok='admin'},
 @{n='POST /api/permissions/assign';m='Post';u='/api/permissions/assign?teacherId=1&canPublish=true';tok='admin'},
 @{n='POST /api/permissions/course/approve';m='Post';u='/api/permissions/course/approve?courseId=1&approved=true';tok='admin'},
 @{n='POST /api/permissions/course/publish';m='Post';u='/api/permissions/course/publish?courseId=1&teacherId=1';tok='admin'},
 @{n='POST /api/course-requests';m='Post';u='/api/course-requests';tok='student';body=@{courseId=2;studentId=1;requestType='OPEN_COURSE';reason='t';statusChangeReason='t'}},
 @{n='POST /api/course-requests/status-change';m='Post';u='/api/course-requests/status-change';tok='teacher';body=@{courseId=1;statusChangeReason='t'}},
 @{n='POST /api/course-requests/review/1';m='Post';u='/api/course-requests/review/1?approve=true&reviewer=admin';tok='admin'},
 @{n='GET /api/course-requests/student/1';m='Get';u='/api/course-requests/student/1';tok='admin'},
 @{n='GET /api/course-requests/pending';m='Get';u='/api/course-requests/pending';tok='admin'},
 @{n='PUT /admin/course-requests/1/approve';m='Put';u='/admin/course-requests/1/approve?approved=true';tok='admin'},
 @{n='GET /api/admin/teacher';m='Get';u='/api/admin/teacher';tok='admin'},
 @{n='POST /api/admin/teacher/add';m='Post';u='/api/admin/teacher/add';tok='admin';body=@{year=2026;deptId=1;index=88}},
 @{n='POST /api/admin/teacher/batch';m='Post';u='/api/admin/teacher/batch';tok='admin';body=@{year=2026;deptId=1;count=1}},
 @{n='DELETE /api/admin/teacher/260010088';m='Delete';u='/api/admin/teacher/260010088';tok='admin'},
 @{n='POST /api/teacher/course/publish';m='Post';u='/api/teacher/course/publish?courseId=1&teacherId=1';tok='teacher'},
 @{n='POST /api/student/select';m='Post';u='/api/student/select';tok='student';body=@{courseCode='CS201';semester='2025秋季'}}
)
$results=@()
foreach($t in $tests){
  $h=@{Authorization="Bearer $((Get-Variable -Name $t.tok).Value)";'Accept'='application/json'}
  try{
    if($t.body){ $raw = if($t.body -is [string]){ $t.body } else { $t.body|ConvertTo-Json -Depth 5 }; $r=Invoke-WebRequest -Method $t.m -Uri "$base$($t.u)" -Headers $h -ContentType 'application/json' -Body $raw -UseBasicParsing }
    else{ $r=Invoke-WebRequest -Method $t.m -Uri "$base$($t.u)" -Headers $h -UseBasicParsing }
    $results+= [pscustomobject]@{API=$t.n; HTTP=$r.StatusCode}
  }catch{
    $code = if($_.Exception.Response){[int]$_.Exception.Response.StatusCode}else{'ERR'}
    $results+= [pscustomobject]@{API=$t.n; HTTP=$code}
  }
}
$results | Format-Table -AutoSize
$bad=$results|Where-Object{$_.HTTP -ge 500 -or $_.HTTP -eq 'ERR'}
Write-Output "TOTAL=$($results.Count)  5xx/ERR=$($bad.Count)"

