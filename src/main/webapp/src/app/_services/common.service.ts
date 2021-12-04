import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { AuthToken } from '@app/_models';
import { Organization } from '@app/_models/organization';
import { Queue } from '@app/_models/queue';
import { PickSlip } from '@app/_models/pick_slip';
import { Container } from '@app/_models/container';

@Injectable({ providedIn: 'root' })
export class CommonService {
    private authSubject = new BehaviorSubject<AuthToken>(null);
    public auth = this.authSubject.asObservable();;

    private orgSubject = new BehaviorSubject<Organization[]>(null);
    public organizations = this.orgSubject.asObservable();

    private queueSubject = new BehaviorSubject<Queue[]>(null);
    public queues = this.queueSubject.asObservable();

    private nextPS = new BehaviorSubject<PickSlip>(null);
    public nextPS$ = this.nextPS.asObservable();

    private containers = new BehaviorSubject<Container[]>(null);
    public containers$ = this.containers.asObservable();

    constructor(
        private router: Router,
        private http: HttpClient
    ) {

    }

    public get authValue(): AuthToken {
        return this.authSubject.value;
    }

    public get pickSlipValue(): PickSlip {
        return this.nextPS.value;
    }

    public async getOrg() {
        const orgs = await this.http.get<Organization[]>(`${environment.apiUrl}/public/organizations`).toPromise();
        this.orgSubject.next(orgs);
    }

    public async getQueues(username, orgcode) {
        const queues = await this.http.get<Queue[]>(`${environment.apiUrl}/public/queues?org_code=${orgcode}&username=${username}`).toPromise();
        this.queueSubject.next(queues);
    }

    public async getNextPickslip() {
        const at: AuthToken = this.authValue;
        const url = `${environment.apiUrl}/api/nextpickslip?org_code=${at.org_code}&user_id=${at.user_id}&picker_id=${at.picker_id}&queue_id=${at.queue_id}`;
        return this.http.get(url).toPromise();
    }

    public async validatePallet(pallet, low, high) {
        const at: AuthToken = this.authValue;
        const url = `${environment.apiUrl}/api/validatePallet?org=${at.org_code}&pallet=${pallet}&tolLow=${low}&tolHigh=${high}`;
        return this.http.get(url).toPromise();
    }

    public async validatePrinter(printer) {
        const at: AuthToken = this.authValue;
        const url = `${environment.apiUrl}/api/validatePrinter?org=${at.org_code}&printer=${printer}`;
        return this.http.get(url).toPromise();
    }

    public async getContainers() {
        const url = `${environment.apiUrl}/api/containers`;
        const conatiners = await this.http.get<Container[]>(url).toPromise();
        this.containers.next(conatiners)
    }

    login(username, password) {
        let payload: HttpParams = new HttpParams();
        payload = payload.set('username', username);
        payload = payload.set('grant_type', 'password');
        payload = payload.set('password', password);
        let headers: HttpHeaders = new HttpHeaders();
        headers = headers.set('Content-Type', 'application/x-www-form-urlencoded');
        return this.http.post<AuthToken>(`${environment.apiUrl}/public/user/login`, payload, { headers })
            .pipe(map(at => {
                this.authSubject.next(at);
                return at;
            }));
    }

    pickSlipAction(pallet, printer, container: Container, action: string) {
        const at: AuthToken = this.authValue;
        const ps: PickSlip = this.pickSlipValue;
        let payload = {
            palletTare: pallet,
            container:container.description,
            tolLow: container.tol_low,
            tolHigh: container.tol_high,
            printer:printer ,
            org: at.org_code,
            userId: at.user_id,
            pickerId: at.picker_id,
            tpl: ps.tpl,
            pickSlipNumber: ps.pick_slip_number,
        }
        return this.http.post(`${environment.apiUrl}/api/${action}`, payload).toPromise();
    }

    logout() {
        this.authSubject.next(null);
        this.router.navigate(['/login']);
    }

    public setNextPS(ps: PickSlip) {
        this.nextPS.next(ps);
    }

}