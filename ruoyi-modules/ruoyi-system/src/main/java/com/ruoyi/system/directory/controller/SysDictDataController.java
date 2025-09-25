package com.ruoyi.system.directory.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.directory.adapter.DictDataTranslator;
import com.ruoyi.system.directory.domain.model.DictData;
import com.ruoyi.system.directory.service.ISysDictDataService;
import com.ruoyi.system.directory.service.ISysDictTypeService;
import com.ruoyi.system.api.domain.SysDictData;
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

/**
 * 数据字典信息 (API facade: Sys* <-> Domain)
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;
    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData sysProbe) {
        startPage();
        // Sys -> Domain
        DictData probe = DictDataTranslator.toDomain(sysProbe);
        List<DictData> domainList = dictDataService.selectDictDataList(probe);

        // Domain -> Sys per risposta API compatibile
        List<SysDictData> sysList = domainList.stream()
                .map(DictDataTranslator::toApi)
                .collect(Collectors.toList());

        return getDataTable(sysList);
    }
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData sysProbe) {
        DictData probe = DictDataTranslator.toDomain(sysProbe);
        List<SysDictData> sysList = dictDataService.selectDictDataList(probe).stream()
                .map(DictDataTranslator::toApi)
                .collect(Collectors.toList());
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        util.exportExcel(response, sysList, "字典数据");
    }
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        DictData d = dictDataService.selectDictDataById(dictCode);
        return success(DictDataTranslator.toApi(d));
    }
    /**
     * 根据字典类型查询字典数据信息
     * (coerente: ritorniamo SysDictData anche qui)
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<DictData> list = dictTypeService.selectDictDataByType(dictType);
        List<SysDictData> sysList = (list == null ? new ArrayList<>() :
                list.stream().map(DictDataTranslator::toApi).collect(Collectors.toList()));
        return success(sysList);
    }
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData sysData) {
        sysData.setCreateBy(SecurityUtils.getUsername());
        DictData data = DictDataTranslator.toDomain(sysData);
        return toAjax(dictDataService.insertDictData(data));
    }
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData sysData) {
        sysData.setUpdateBy(SecurityUtils.getUsername());
        DictData data = DictDataTranslator.toDomain(sysData);
        return toAjax(dictDataService.updateDictData(data));
    }
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
