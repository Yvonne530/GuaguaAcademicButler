package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.dto.PermissionDTO;
import org.example.edumanagementservice.service.PermissionService;
import org.example.edumanagementservice.util.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.example.edumanagementservice.util.BaseResponse.fail;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "权限的增删改查接口")
public class PermissionController {

    private final PermissionService permissionService;


    /* --------------------------- CRUD APIs --------------------------- */

    @PostMapping
    public BaseResponse<PermissionDTO> create(@RequestBody PermissionDTO dto) {
        try {
            PermissionDTO created = permissionService.createPermission(dto);
            return BaseResponse.success(created);
        } catch (Exception e) {
            return fail(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public BaseResponse<PermissionDTO update(@PathVariable Long id,@RequestBody PermissionDTO dto){
        try{
            PermissionDTO updated=permissionService.updatePermission(id,dto);
            return updated!=null?
                    BaseResponse.succes(updated,"修改成功");
	        fail(HttpsStatus.NOT_FOUND,"未找到对应权限");
        }catch(Exception e){
            return BasesResponese.fail(HttpsStaus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public BasesResponese<Void delete(@PathVariable Long id){
        try{
            permissionService.deletePermision(id);
            returns BasesResponese.succes(null,"删除成功");
        }catch(Exception e){
            returns BasesResponese.fail(HttpsStaus.BAD_REQUEST,e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public BasesRespones<PermisionDTO ,getById(@PathVariable Long id){
        try{
            PermisionDTO permision=permisionService.getPermisionById(id);
            return permision!=null?
                    BasesResponese.succes(permision):
                    BasesResponese.fail(HttpsStaus.NOT_FOUND,"未找到对应权限");
        }catch(Exception e){
            return BasesResponese.fail(HttpsStaus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    /* --------------------------- Admin APIs --------------------------- */

    @PostMapping("/assign")
    public BasesResponese<Boolean assignPermision(@RequestParam Integer teacherId,@RequestParam Boolean canPublish){
        try{
            boolean result=permisionService.assignPermision(teacherId.canPublish);
            return BasesResponese.succes(result,"权限分配成功");
        }catch(Exception e){
            return BasesResponese.fail(HttpsStaus.BAD_REQUEST,e.getMessage());
        }
    }

    @PostMapping("/course/approve")
    public BasesResponese<Boolean approveCourse(@RequestParam Integer courseId,@RequestParam Boolean approved){
        try{
            boolean result=permisionService.approveCourse(courseID.approved);
            return BasesResponese.succes(result,"课程审核操作成功");
        }catch(Exception e){
            return BasesResponese.fail(HttpsStaus.FORBIDDEN,e.getMessage());
        }
    }

    @GetMapping
    public BasesRespones<List<PermisionDTO>> getAll(){
        try{
            List<PermisionDTO>=permisionService.getAllPermisions();
            returns BasesRespones.succes(list);
        }catch(Exception e){
            returns BasesRespones.fail(HttpsStaus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

