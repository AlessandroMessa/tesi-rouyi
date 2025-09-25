package com.ruoyi.system.directory.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.system.directory.adapter.DictTypeTranslator;
import com.ruoyi.system.directory.domain.model.DictType;
import com.ruoyi.system.directory.service.ISysDictTypeService;
import com.ruoyi.system.api.domain.SysDictType;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType sysProbe) {
        startPage();
        DictType probe = DictTypeTranslator.toDomain(sysProbe);
        List<SysDictType> list = dictTypeService.selectDictTypeList(probe).stream()
                .map(DictTypeTranslator::toApi)
                .collect(Collectors.toList());
        return getDataTable(list);
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType sysProbe) {
        DictType probe = DictTypeTranslator.toDomain(sysProbe);
        List<SysDictType> list = dictTypeService.selectDictTypeList(probe).stream()
                .map(DictTypeTranslator::toApi)
                .collect(Collectors.toList());
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return success(DictTypeTranslator.toApi(dictTypeService.selectDictTypeById(dictId)));
    }

    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType sysDict) {
        DictType dict = DictTypeTranslator.toDomain(sysDict);
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType sysDict) {
        DictType dict = DictTypeTranslator.toDomain(sysDict);
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> list = dictTypeService.selectDictTypeAll().stream()
                .map(DictTypeTranslator::toApi)
                .collect(Collectors.toList());
        return success(list);
    }
}
