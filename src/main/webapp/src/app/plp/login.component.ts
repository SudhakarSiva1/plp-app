import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { CommonService, AlertService } from '@app/_services';
import { Organization } from '@app/_models/organization';
import { Queue } from '@app/_models/queue';
import _ from 'lodash';

@Component({ templateUrl: 'login.component.html' })
export class LoginComponent implements OnInit {
    form: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    public organizations: Organization[] = [];
    public queues: Queue[] = [];

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
        this.form = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required],
            organization: ['', Validators.required],
            queue: ['', Validators.required],
        });
        this.commonService.organizations.subscribe(data => {
            if (!_.isEmpty(data)) {
                this.organizations = data;
                this.f.organization.setValue(data[0].org_code);
            }
            console.log(this.organizations);
        });
        this.commonService.queues.subscribe(data => {
            if (!_.isEmpty(data)) {
                this.queues = data;
                this.f.queue.setValue(data[0].queue_id);
            }
            console.log(this.queues);
        });
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    get f() { return this.form.controls; }

    onFocusOutEvent(event: any) {
        if (_.isEmpty(event.target.value)) {
            this.queues = []
            this.f.queue.setValue("");
        }
        else this.commonService.getQueues(event.target.value, this.f.organization.value);
    }

    onOrgChange(ev: any) {
        if (_.isEmpty(this.f.username.value)) {
            this.queues = []
            this.f.queue.setValue("");
        }
        else this.commonService.getQueues(this.f.organization.value, ev.target.value);
    }

    onSubmit() {
        this.submitted = true;
        this.alertService.clear();
        if (this.form.invalid) {
            return;
        }
        this.loading = true;
        const details= `${this.f.username.value}::${this.f.organization.value}::${this.f.queue.value}`
        this.commonService.login(details, this.f.password.value)
            .pipe(first())
            .subscribe(data => {
                    this.router.navigate(["/home"]);
                }, error => {
                    this.alertService.error(error);
                    this.loading = false;
                });
    }
}