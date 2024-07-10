import {Routes} from '@angular/router';
import {SignInComponent} from "./component/sign-in/sign-in.component";
import {SignUpComponent} from "./component/sign-up/sign-up.component";
import {DashboardComponent} from "./component/dashboard/dashboard.component";
import {BrowseTeamsComponent} from "./component/browse-teams/browse-teams.component";
import {BrowseUsersComponent} from "./component/browse-users/browse-users.component";
import {TeamDetailsComponent} from "./component/team-details/team-details.component";
import {CreateTeamComponent} from "./component/create-team/create-team.component";
import {EditTeamComponent} from "./component/edit-team/edit-team.component";
import { AuthGuard } from './service/authentication/auth.guard';

export const routes: Routes = [
  { path: '', component: SignInComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'sign-in', component: SignInComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'teams', component: BrowseTeamsComponent, canActivate: [AuthGuard]  },
  { path: 'users', component: BrowseUsersComponent, canActivate: [AuthGuard]  },
  { path: 'teams/:id', component: TeamDetailsComponent, canActivate: [AuthGuard]  },
  { path: 'create-team', component: CreateTeamComponent, canActivate: [AuthGuard]  },
  { path: 'teams/:id/edit-team', component: EditTeamComponent, canActivate: [AuthGuard]  },

];
