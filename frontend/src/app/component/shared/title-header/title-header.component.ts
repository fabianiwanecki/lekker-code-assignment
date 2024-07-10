import {Component, Input} from '@angular/core';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-title-header',
  standalone: true,
  imports: [
    MatIconButton,
    MatIcon,
    MatButton,
    RouterLink
  ],
  templateUrl: './title-header.component.html',
  styleUrl: './title-header.component.scss'
})
export class TitleHeaderComponent {

  @Input()
  title: string = "";

  @Input()
  titleRouterLink: string = "#";
}
