import {BrowserModule} from '@angular/platform-browser';
import {DEFAULT_CURRENCY_CODE, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {NgbDateAdapter, NgbDateParserFormatter, NgbModule, NgbTimeAdapter} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {RegistrationComponent} from './components/registration/registration.component';
import {NotificationComponent} from './components/notification/notification.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {EditProfileComponent} from './components/profile/edit/edit.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {CustomDateAdapter, CustomDateParserFormatter} from './interceptors/custom-date-parser';
import {AddressFieldsComponent} from './components/address-fields/address-fields.component';
import {EventSearchComponent} from './components/event-search/event-search.component';
import {DescriptionPipe} from './pipes/description.pipe';
import {CustomTimeAdapter} from './interceptors/custom-time-parser';
import {EventTypeSelectComponent} from './components/event-type-select/event-type-select.component';
import {DurationPickerModule} from 'ngx-duration-picker';
import {EventCreatorComponent} from './components/event-creator/event-creator.component';
import {EventDetailComponent} from './components/event-detail/event-detail.component';
import {CreateVenueComponent} from './components/create-venue/create-venue.component';
import {SeatingchartEditorComponent} from './components/seatingchart-editor/seatingchart-editor.component';
import {SeatingChartInteractionDirective} from './directive/seating-chart-interaction.directive';
import {TicketComponent} from './components/ticket/ticket.component';
import {Ng2ImgMaxModule} from 'ng2-img-max';
import {ArtistSelectComponent} from './components/event-creator/artist-select/artist-select.component';
import {VenueSelectComponent} from './components/venue-select/venue-select.component';
import {HallSelectComponent} from './components/hall-select/hall-select.component';
import {SeatingChartSelectComponent} from './components/seating-chart-select/seating-chart-select.component';
import {AdminCreateUserComponent} from './components/admin-create-user/admin-create-user.component';
import {ShowCreatorComponent} from './components/show-creator/show-creator.component';
import {SeatingChartViewerComponent} from './components/seating-chart-viewer/seating-chart-viewer.component';
import {OrdersComponent} from './components/orders/orders.component';
import {OrderDetailsComponent} from './components/order-details/order-details.component';
import {UserSearchComponent} from './components/user-search/user-search.component';
import {AdminShowUserComponent} from './components/profile/admin-show-user/admin-show-user.component';
import {CheckoutSeatingChartComponent} from './components/checkout/seating-chart/checkout-seating-chart.component';
import {CheckoutPaymentComponent} from './components/checkout/checkout-payment/checkout-payment.component';
import {CheckoutNavigationComponent} from './components/checkout/checkout-navigation/checkout-navigation.component';
import {CheckoutOverviewComponent} from './components/checkout/checkout-overview/checkout-overview.component';
import {CheckoutConfirmationComponent} from './components/checkout/checkout-confirmation/checkout-confirmation.component';
import {MessageDetailComponent} from './components/message-detail/message-detail.component';
import {SidebarModule} from 'ng-sidebar';
import {HomeEventCardComponent} from './components/home-event-card/home-event-card.component';
import {LoadingSpinnerComponent} from './components/loading-spinner/loading-spinner.component';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        FooterComponent,
        HomeComponent,
        LoginComponent,
        MessageComponent,
        EditProfileComponent,
        PageNotFoundComponent,
        AddressFieldsComponent,
        RegistrationComponent,
        NotificationComponent,
        SeatingchartEditorComponent,
        SeatingChartInteractionDirective,
        CreateVenueComponent,
        EventSearchComponent,
        DescriptionPipe,
        EventTypeSelectComponent,
        EventCreatorComponent,
        EventDetailComponent,
        TicketComponent,
        ArtistSelectComponent,
        VenueSelectComponent,
        HallSelectComponent,
        AdminCreateUserComponent,
        SeatingChartSelectComponent,
        ShowCreatorComponent,
        SeatingChartViewerComponent,
        UserSearchComponent,
        AdminShowUserComponent,
        CheckoutSeatingChartComponent,
        CheckoutPaymentComponent,
        CheckoutNavigationComponent,
        CheckoutOverviewComponent,
        CheckoutConfirmationComponent,
        MessageDetailComponent,
        OrdersComponent,
        OrderDetailsComponent,
        HomeEventCardComponent,
        LoadingSpinnerComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        NgbModule,
        FormsModule,
        NgSelectModule,
        DurationPickerModule,
        Ng2ImgMaxModule,
        SidebarModule
    ],
    providers: [httpInterceptorProviders,
        {provide: NgbDateAdapter, useClass: CustomDateAdapter},
        {provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter},
        {provide: NgbTimeAdapter, useClass: CustomTimeAdapter},
        {provide: DEFAULT_CURRENCY_CODE, useValue: 'EUR' }
    ],
    bootstrap: [AppComponent]
})

export class AppModule {
}
