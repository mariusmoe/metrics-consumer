import { Component, OnInit } from '@angular/core';
import {AchievementService} from "../../_services/achievement.service";
import {Achievement} from "../../_models/achievement";
import {UserAchievement} from "../../_models/user-achievement";

@Component({
  selector: 'app-achievements',
  templateUrl: './achievements.component.html',
  styleUrls: ['./achievements.component.css']
})
export class AchievementsComponent implements OnInit {

  achievementList: Achievement[];
  userAchievementList: UserAchievement[];


  constructor(
    private achievementService: AchievementService
  ) { }

  ngOnInit() {
    this.getAllAchievementData();
  }

  // getAllAchievements() {
  //   this.achievementService.getAllAchievementsFromService().subscribe(achievementList => {
  //     this.achievementList = achievementList;
  //     console.log(this.achievementList);
  //   });
  // }
  //
  // getAllUserAchievements() {
  //   console.log("getAllUserAchievements called!!!");
  //   this.achievementService.getAllUserAchievementsFromService().subscribe(userAchievementList => {
  //     this.userAchievementList = userAchievementList;
  //     console.log(this.userAchievementList);
  //   });
  // }

  getUserAchievement(achievementRef: string):UserAchievement {
    // console.log(achievementRef);
    return this.userAchievementList.find(obj => {
      return obj.achievementRef === achievementRef;
    });
  }

  private getAllAchievementData() {
    this.achievementService.getAllAchivementDataFromService().subscribe(responseList => {
      this.achievementList = responseList[0];
      this.userAchievementList = responseList[1];
    });
  }
}
