import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home';
import { PickslipComponent } from './pickslip/pickslip.component';
import { LoginComponent } from './plp/login.component';
import { AuthGuard } from './_helpers';

const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'home', canActivate: [AuthGuard], component: HomeComponent },
    { path: 'pickslip', canActivate: [AuthGuard], component: PickslipComponent },
    { path: '**', redirectTo: '' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }