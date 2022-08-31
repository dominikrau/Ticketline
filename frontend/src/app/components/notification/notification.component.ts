import {Component, OnInit} from '@angular/core';
import {NotificationService} from '../../services/notification.service';
import {Notification} from '../../dtos/notification';

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

    notifications: Notification[] = [];

    constructor(private notifService: NotificationService) {
    }

    ngOnInit(): void {
        this.notifService.notifications.subscribe(notif => {
            this.notifications.push(notif);
            window.setTimeout(
                () => this.notifications = this.notifications.filter(x => x !== notif), 3500
            );
        });
    }

    close(index: any) {
        this.notifications.splice(index, 1);
    }
}
