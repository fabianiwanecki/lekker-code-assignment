import {Component, Input} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-app-bar',
  standalone: true,
  imports: [
    MatIcon,
    MatIconButton,
    RouterLink
  ],
  templateUrl: './app-bar.component.html',
  styleUrl: './app-bar.component.scss'
})
export class AppBarComponent {

  @Input()
  headline: string = "";

  @Input()
  backButtonRouterLink: string = "#";

  @Input()
  title: string = '';

}
