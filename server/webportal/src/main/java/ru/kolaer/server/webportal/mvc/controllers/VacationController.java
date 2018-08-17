package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

@RestController
@RequestMapping(value = "/vacations")
@Api(tags = "Отпуска")
@Slf4j
public class VacationController {
    private final VacationService vacationService;

    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @ApiOperation(value = "Получить отпуска")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<VacationDto> getVacations(FindVacationPageRequest request) {
        return vacationService.getVacations(request);
    }

    @ApiOperation(value = "Получить периоды")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/periods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<VacationPeriodDto> getVacationPeriods(FindVacationPeriodPageRequest request) {
        return vacationService.getVacationPeriods(request);
    }

    @ApiOperation(value = "Получить баланс")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/balance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationBalanceDto getBalance(FindBalanceRequest request) {
        return vacationService.getBalance(request);
    }

}
