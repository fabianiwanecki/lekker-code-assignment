import {Routes} from '@angular/router';
import {SignInComponent} from "./component/sign-in/sign-in.component";
import {SignUpComponent} from "./component/sign-up/sign-up.component";
import {DashboardComponent} from "./component/dashboard/dashboard.component";
import {BrowseTeamsComponent} from "./component/browse-teams/browse-teams.component";
import {BrowseUsersComponent} from "./component/browse-users/browse-users.component";
import {TeamDetailsComponent} from "./component/team-details/team-details.component";
import {CreateTeamComponent} from "./component/create-team/create-team.component";
import {EditTeamComponent} from "./component/edit-team/edit-team.component";
import { authGuard } from './service/authentication/auth.guard';

export const routes: Routes = [
  { path: '', component: SignInComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'sign-in', component: SignInComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'teams', component: BrowseTeamsComponent, canActivate: [authGuard]  },
  { path: 'users', component: BrowseUsersComponent, canActivate: [authGuard]  },
  { path: 'teams/:id', component: TeamDetailsComponent, canActivate: [authGuard]  },
  { path: 'create-team', component: CreateTeamComponent, canActivate: [authGuard]  },
  { path: 'teams/:id/edit-team', component: EditTeamComponent, canActivate: [authGuard]  },

];
