import { Component } from '@angular/core';

import { CommonService } from './_services';
import { AuthToken } from './_models';

@Component({ selector: 'app', templateUrl: 'app.component.html' })
export class AppComponent {
    auth: AuthToken;

    constructor(private commonService: CommonService) {
        this.commonService.getOrg();
        this.commonService.getContainers();
        this.commonService.auth.subscribe(x => this.auth = x);
    }

    logout() {
        this.commonService.logout();
    }
}