import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {EditProfileComponent} from './components/profile/edit/edit.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {EventCreatorComponent} from './components/event-creator/event-creator.component';
import {EventDetailComponent} from './components/event-detail/event-detail.component';
import {EventSearchComponent} from './components/event-search/event-search.component';
import {SeatingchartEditorComponent} from './components/seatingchart-editor/seatingchart-editor.component';
import {CreateVenueComponent} from './components/create-venue/create-venue.component';
import {TicketComponent} from './components/ticket/ticket.component';
import {AdminGuard} from './guards/admin.guard';
import {ShowCreatorComponent} from './components/show-creator/show-creator.component';
import {CheckoutSeatingChartComponent} from './components/checkout/seating-chart/checkout-seating-chart.component';
import {CheckoutPaymentComponent} from './components/checkout/checkout-payment/checkout-payment.component';
import {CheckoutConfirmationComponent} from './components/checkout/checkout-confirmation/checkout-confirmation.component';
import {UserSearchComponent} from './components/user-search/user-search.component';
import {MessageDetailComponent} from './components/message-detail/message-detail.component';
import {OrdersComponent} from './components/orders/orders.component';
import {OrderDetailsComponent} from './components/order-details/order-details.component';

const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'login', component: LoginComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
    {path: 'message/:id', canActivate: [AuthGuard], component: MessageDetailComponent},
    {path: 'profile/edit', canActivate: [AuthGuard], component: EditProfileComponent},
    {path: 'seating-chart', canActivate: [AuthGuard], component: SeatingchartEditorComponent},
    {path: 'venue', canActivate: [AuthGuard], component: CreateVenueComponent},
    {path: 'search', canActivate: [AuthGuard], component: EventSearchComponent},
    {path: 'events/:id', canActivate: [AuthGuard], component: EventDetailComponent},
    {path: 'search/events/:id', canActivate: [AuthGuard], component: EventDetailComponent},
    {path: 'tickets/:id', canActivate: [AuthGuard], component: TicketComponent},
    {path: 'orders', canActivate: [AuthGuard], component: OrdersComponent},
    {path: 'orders/:id', canActivate: [AuthGuard], component: OrderDetailsComponent},
    {path: 'checkout/:id', canActivate: [AuthGuard], component: CheckoutSeatingChartComponent},
    {path: 'checkout/:id/payment', canActivate: [AuthGuard], component: CheckoutPaymentComponent},
    {path: 'checkout/:id/confirm', canActivate: [AuthGuard], component: CheckoutConfirmationComponent},
    {
        path: 'admin', canActivateChild: [AuthGuard, AdminGuard], children: [
            {path: 'events/create', component: EventCreatorComponent},
            {path: 'events/:id/create-show', component: ShowCreatorComponent},
            {path: 'events/:id/create-show/create-seating-chart', component: SeatingchartEditorComponent},
            {path: 'users', component: UserSearchComponent}
        ]
    },
    {path: '**', component: PageNotFoundComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
