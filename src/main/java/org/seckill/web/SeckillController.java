package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author whitedot
 * @date 2018/11/26
 * @description url:/模块/资源/{id}/细分/seckill/list
 **/
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }


    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        //获取列表页
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> seckillResult;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            seckillResult = new SeckillResult<>(false, e.getMessage());
        }
        return seckillResult;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            // SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return new SeckillResult<>(true, seckillExecution);
        } catch (SeckillCloseException e1) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e2) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        } catch (Exception e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true, seckillExecution);
        }
    }

    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
