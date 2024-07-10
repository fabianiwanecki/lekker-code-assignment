import {Component, inject} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {UserWithRankDto} from "../../service/user/user.service";

@Component({
  selector: 'app-dialog-member-request',
  standalone: true,
  imports: [MatButtonModule, MatDialogModule],
  templateUrl: './dialog-member-request.component.html',
  styleUrl: './dialog-member-request.component.scss'
})
export class DialogMemberRequestComponent {

  readonly dialogRef = inject(MatDialogRef<DialogMemberRequestComponent>);
  readonly data = inject<any>(MAT_DIALOG_DATA);

  onDecline() {
    this.dialogRef.close(false);
  }

  onApprove() {
    this.dialogRef.close(true);
  }
}
