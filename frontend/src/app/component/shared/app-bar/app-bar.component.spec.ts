import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AppBarComponent} from './app-bar.component';
import {provideRouter, Routes} from "@angular/router";

const routes: Routes = [
  {path: '', component: {} as any},
];

describe('AppBarComponent', () => {
  let component: AppBarComponent;
  let fixture: ComponentFixture<AppBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppBarComponent],
      providers: [provideRouter(routes)]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
