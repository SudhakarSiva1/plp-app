import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthToken } from '@app/_models';
import { PickSlip } from '@app/_models/pick_slip';
import { AlertService, CommonService } from '@app/_services';
import { first } from 'rxjs/operators';

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent {
    auth: AuthToken;
    public loading: boolean = false;
    constructor(
        private commonService: CommonService,
        private alertService:AlertService,
        private router: Router) {
        this.auth = this.commonService.authValue;
    }

    pickslip() {
        this.alertService.clear();
        this.loading = true;
        this.commonService.getNextPickslip().then((data:any) => {
            this.loading = false;
            if (data.XC_RETURN_STATUS == 'E') {
                this.alertService.error(data.XC_ERROR_MESSAGE);
            } else {
                this.alertService.success(data.XC_ERROR_MESSAGE);
                const ps = new PickSlip(data.XN_PICKSLIP, data.XC_CUSTOMER_NAME, data.XC_TPL,data.XN_PALLET_TARE, data.XC_CONTAINER_TYPE);
                this.commonService.setNextPS(ps);
                this.router.navigate(['/pickslip']);
            }
            console.log(data);
            }, error => {
                this.alertService.error(error);
                this.loading = false;
            });
    }
}