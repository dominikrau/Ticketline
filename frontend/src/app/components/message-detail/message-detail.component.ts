import { Component, OnInit } from '@angular/core';
import {Message} from '../../dtos/message';
import {MessageService} from '../../services/message.service';
import {AuthService} from '../../services/auth.service';
import {NotificationService} from '../../services/notification.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-message-detail',
  templateUrl: './message-detail.component.html',
  styleUrls: ['./message-detail.component.css']
})
export class MessageDetailComponent implements OnInit {

    error: boolean = false;
    errorMessage: string = '';
    messageId: number;
    message: Message;


    constructor(private route: ActivatedRoute,
        private messageService: MessageService,
        private authService: AuthService,
        private notificationService: NotificationService) {
    }

    ngOnInit(): void {
        this.loadMessageDetails();
        this.markAsRead(this.messageId);
    }

    /**
     * Loads the text of message and update the existing array of message
     */
    loadMessageDetails() {
        this.messageId = +this.route.snapshot.paramMap.get('id');
        this.messageService.getMessageById(this.messageId).subscribe(
            (message: Message) => {
                this.message = message;
                console.log('Message was successfully loaded');
            },
            error => this.notificationService.error(error)
        );
    }

    markAsRead(id: number) {
        this.messageService.markAsRead(id).subscribe(
            success => console.log('Message was marked as read'),
            error => this.notificationService.error(error)
        );
    }
}
