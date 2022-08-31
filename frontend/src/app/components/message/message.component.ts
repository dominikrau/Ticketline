import {Component, OnInit, ViewChild} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Message} from '../../dtos/message';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Page} from '../../dtos/page';
import {NotificationService} from '../../services/notification.service';
import {Ng2ImgMaxService} from 'ng2-img-max';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-message',
    templateUrl: './message.component.html',
    styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {


    messageForm: FormGroup;
    messages: Page<Message>;
    pageSize: number = 5;
    pageNumber: number = 1;
    readToggle: boolean = false;

    @ViewChild('content')
    modalTemplate;

    constructor(private messageService: MessageService,
                private formBuilder: FormBuilder,
                private authService: AuthService,
                private imageResize: Ng2ImgMaxService,
                private notificationService: NotificationService,
                private modalService: NgbModal) {
        this.messageForm = this.formBuilder.group({
            title: ['', [Validators.required]],
            summary: ['', [Validators.required]],
            text: ['', [Validators.required]],
            imageUrl: ['', [Validators.required]],
        });
    }

    ngOnInit() {
        this.loadUnreadMessages();
    }

    /**
     * Returns true if the authenticated user is an admin
     */
    isAdmin(): boolean {
        return this.authService.getUserRole() === 'ADMIN';
    }

    dismiss(modal) {
        /*this.hallForm.reset({
            venue: this.getVenue()
        });*/
        modal.dismiss();
    }

    openModal = () => {
        return this.modalService.open(this.modalTemplate, {size: 'xl', windowClass: 'fade-in'}).result;
    }

    /**
     * Validates the form and creates a new message
     * If the procedure was successful, the form will be cleared.
     */
    addMessage(modal) {
        Object.keys(this.messageForm.value).forEach((key) => {
            if (typeof this.messageForm.get(key).value === 'string') {
                this.messageForm.get(key).setValue(this.messageForm.get(key)?.value.trim());
            }
        });
        this.messageForm.markAllAsTouched();
        if (this.messageForm.valid) {
            this.messageService.createMessage(this.messageForm.value).subscribe(
                () => {
                    modal.dismiss();
                    this.loadMessages();
                },
                error => {
                    this.notificationService.error(error);
                }
            );
            this.clearForm();
        }
    }

    /**
     * Shows the specified message details. If it is necessary, the details text will be loaded
     * @param message which details should be shown
     */
    getMessageDetails(message: Message) {
        this.loadMessageDetails(message);
    }

    /**
     * Loads the text of message and update the existing array of message
     * @param mes: message which details should be loaded
     */
    loadMessageDetails(mes: Message) {
        this.messageService.getMessageById(mes.id).subscribe(
            (message: Message) => {
                const result = this.messages.content.find(x => x.id === mes.id);
                result.text = message.text;
            },
            error => {
                this.notificationService.error(error);
            }
        );
    }

    /**
     * decides whether to load read or unread messages
     */
    private loadMessages() {
        if (this.readToggle === false) {
            this.loadUnreadMessages();
        } else {
            this.loadReadMessages();
        }
    }

    /**
     * Loads the specified page of unread messages from the backend
     */
    private loadUnreadMessages() {
        this.messageService.getUnreadMessages(this.pageSize, this.pageNumber - 1).subscribe(
            messages => this.messages = messages,
            error => {
                console.log(error);
                this.notificationService.error(error);
                this.messages = undefined;
            }
        );
    }

    /**
     * Loads the specified page of read messages from the backend
     */
    private loadReadMessages() {
        this.messageService.getReadMessages(this.pageSize, this.pageNumber - 1).subscribe(
            messages => this.messages = messages,
            error => {
                console.log(error);
                this.notificationService.error(error);
                this.messages = undefined;
            }
        );
    }

    changePage(page: number) {
        this.pageNumber = page;
        if (this.readToggle === false) {
            this.loadUnreadMessages();
        } else {
            this.loadReadMessages();
        }
    }

    toggleReadUnread() {
        if (this.readToggle === false) {
            this.readToggle = true;
            this.pageNumber = 1;
            this.loadReadMessages();
        } else {
            this.readToggle = false;
            this.pageNumber = 1;
            this.loadUnreadMessages();
        }
    }

    onImageInputChanged(event) {
        const image = event.target.files[0];
        if (image === undefined) {
            this.messageForm.controls.imageUrl.setValue('');
            return;
        }
        const reader = new FileReader();
        const that = this;

        this.imageResize.resizeImage(image, 800, 600).subscribe(
            result => {
                console.log(result);
                reader.readAsDataURL(result);
                reader.onloadend = function () {
                    that.messageForm.controls.imageUrl.setValue(reader.result.toString());
                };
            },
            () => {
                this.notificationService.errorMessage('error while resizing image!');
            }
        );
        this.messageForm.controls.imageUrl.markAllAsTouched();
    }

    private clearForm() {
        this.messageForm.reset();
    }

    isInvalid(controlName: string) {
        const control = this.messageForm.controls[controlName];
        return control.touched && control.errors;
    }

}
