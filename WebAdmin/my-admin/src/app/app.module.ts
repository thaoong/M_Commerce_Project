import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { AdminCategoryComponent } from './admin-category/admin-category.component';
import { AdminCustomerComponent } from './admin-customer/admin-customer.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { AdminNavigateBarComponent } from './admin-navigate-bar/admin-navigate-bar.component';
import { AdminOrderComponent } from './admin-order/admin-order.component';
import { AdminProductComponent } from './admin-product/admin-product.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminHomeComponent,
    AdminCategoryComponent,
    AdminCustomerComponent,
    AdminLoginComponent,
    AdminNavigateBarComponent,
    AdminOrderComponent,
    AdminProductComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
