import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Container } from '@app/_models/container';
import { PickSlip } from '@app/_models/pick_slip';
import { AlertService, CommonService } from '@app/_services';
import _ from 'lodash';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-pickslip',
  templateUrl: './pickslip.component.html',
  styleUrls: ['./pickslip.component.less']
})
export class PickslipComponent implements OnInit {

  public pickslip: PickSlip;
  form: FormGroup;
  printLoading = false;
  pickingLoading = false;
  public containers: Container[] = [];
  public submitted: boolean;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private commonService: CommonService,
    private alertService: AlertService) {
    this.pickslip = this.commonService.pickSlipValue;
    this.form = this.formBuilder.group({
      container: ['', Validators.required],
      pallet: ['', Validators.required],
      printer: ['', Validators.required],
      // queue: ['', Validators.required],
    });
  }

  ngOnInit(): void {

    this.commonService.containers$.subscribe(data => {
      if (data) {
        this.containers = data;
        this.f.container.setValue(data[0].flex_value);
      }
    })

  }

  get f() { return this.form.controls; }

  onContainerChange(ev: any) {
    this.alertService.clear();
  }

  onPalletFocusOut(event: any) {
    this.alertService.clear();
    if (!_.isEmpty(event.target.value)) {
      const selected: Container = this.containers.find(c => c.flex_value == this.f.container.value);
      if (selected) {
        this.commonService.validatePallet(event.target.value, selected.tol_low, selected.tol_high).then((data: any) => {
          if (data && data.XC_RETURN_STATUS == 'E') {
            this.alertService.error(data.XC_ERROR_MESSAGE);
          }
        });
      }
    }
  }

  onPrinterFocusOut(event: any) {
    this.alertService.clear();
    if (!_.isEmpty(event.target.value)) {
      this.commonService.validatePrinter(event.target.value).then((data: any) => {
        if (data && data.XC_RETURN_STATUS == 'E') {
          this.alertService.error(data.XC_ERROR_MESSAGE);
        } else {
          this.alertService.success(data.XC_ERROR_MESSAGE);
        }
      });
    }
  }

  submitForm(action) {
    this.submitted = true;
    this.alertService.clear();
    if (this.form.invalid) return;
    const selected: Container = this.containers.find(c => c.flex_value == this.f.container.value);
    if (action == 'print') {
      this.printLoading = true;
      
      this.commonService.pickSlipAction(this.f.pallet.value, this.f.printer.value, selected, 'printPickSlip' ).then((data: any) => {
        this.printLoading = false;
        if (data && data.XC_RETURN_STATUS == 'E') {
          this.alertService.error(data.XC_ERROR_MESSAGE);
        } else {
          this.alertService.success(data.XC_ERROR_MESSAGE);
        }
      });
    } else {
      this.pickingLoading = true;
      this.commonService.pickSlipAction(this.f.pallet.value, this.f.printer.value, selected, 'picking').then((data: any) => {
        this.pickingLoading = false;
        if (data && data.XC_RETURN_STATUS == 'E') {
          this.alertService.error(data.XC_ERROR_MESSAGE);
        } else {
          this.alertService.success(data.XC_ERROR_MESSAGE);
        }
      });
    }
  }

}
